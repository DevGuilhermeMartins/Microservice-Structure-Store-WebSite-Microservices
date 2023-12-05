package br.com.workcode.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.workcode.model.Product;
import br.com.workcode.proxy.CambioProxy;
import br.com.workcode.proxy.CatalogProxy;
import br.com.workcode.repository.ProductRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Product Endpoints")
@RestController
@RequestMapping("/product-service")
@RequiredArgsConstructor
public class ProductController {

	private final ProductRepository productRepository;

	private final CambioProxy proxyCambio;

	private final CatalogProxy proxyCatalog;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Operation(summary = "Create a Product")
	@PostMapping
	public Product save(@RequestBody Product product) {
		productRepository.save(product);
		String routingKey = "orders.v1.product-created";
		rabbitTemplate.convertAndSend(routingKey, product.getId());
		return product;
	}

	@Operation(summary = "Find All Products")
	@GetMapping
	@CircuitBreaker(name = "findAllCircuit", fallbackMethod = "fallBackFindAll")
	public List<Product> findAllProducts() {
		List<Product> products = productRepository.findAll();
		for (Product product : products) {
			var productTax = proxyCatalog.getTaxCatalog(product.getCatalog(), product.getPrice());
			product.setPrice(productTax.getTotalValue());
		}
		return products;
	}

	@Operation(summary = "Find a Product By your Id and make a conversion by currency")
	@GetMapping("/conversion/{id}")
	@CircuitBreaker(name = "findByIdAndCurrency", fallbackMethod = "FallBackProduct")
	public Product findByIdAndCurrency(@PathVariable Long id, @RequestParam String currency) {
		var product = productRepository.findById(id).orElseThrow(() -> new RuntimeException());

		var catalogItem = proxyCatalog.getTaxCatalog(product.getCatalog(), product.getPrice());
		var cambio = proxyCambio.getCambio(product.getPrice(), "USD", currency, catalogItem.getTotalValue());
		product.setPrice(cambio.getConvertedValue());
		return product;
	}

	@Operation(summary = "Filters all products by your catalog")
	@GetMapping("/filter")
	@CircuitBreaker(name = "findAllProdCatalog", fallbackMethod = "fallBackCatalogProd")
	public List<Product> findAllProductsByCatalog(@RequestParam String catalog) {
		List<Product> products = productRepository.findProductByCatalog(catalog);
		if (products == null)
			new RuntimeException("Catalog product doesn't search");
		for (Product product : products) {
			var productCatalog = proxyCatalog.getTaxCatalog(product.getCatalog(), product.getPrice());
			product.setPrice(productCatalog.getTotalValue());
		}
		return products;
	}

	@Operation(summary = "Find a Product By your Id with your catalog tax")
	@GetMapping("/item/{id}")
	@CircuitBreaker(name = "findById", fallbackMethod = "fallBackFindById")
	public Product findItemById(@PathVariable Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Resource not found"));
		var productCatalog = proxyCatalog.getTaxCatalog(product.getCatalog(), product.getPrice());
		product.setPrice(productCatalog.getTotalValue());
		return product;
	}

	public List<Product> fallBackFindAll(Exception e) {
		List<Product> products = productRepository.findAll();
		for (Product product : products) {
			product.setId(null);
			product.setProductName("Product unavailable");
			product.setCatalog("Catalog unavailable");
			product.setPrice(BigDecimal.valueOf(0.0));
		}
		return products;
	}

	public Product fallBackFindById(Long id, Exception e) {
		String prodName = "Product unavailable";
		BigDecimal prodPrice = BigDecimal.valueOf(0.0);
		String catalog = "Catalog unavailable";
		return new Product(null, prodName, catalog, prodPrice);
	}

	public List<Product> fallBackCatalogProd(String catalog, Exception e) {
		List<Product> products = productRepository.findProductByCatalog(catalog);
		for (Product product : products) {
			product.setId(null);
			product.setProductName("Product unavailable");
			product.setCatalog("Catalog unavailable");
			product.setPrice(BigDecimal.valueOf(0.0));
		}
		return products;
	}

	public Product FallBackProduct(Long id, String currency, Exception e) {
		String prodName = "Product unavailable";
		BigDecimal prodPrice = BigDecimal.valueOf(0.0);
		String catalog = "Catalog unavailable";
		return new Product(null, prodName, catalog, prodPrice);
	}

	@Operation(summary = "Update a Product")
	@PutMapping("/{id}")
	public Product update(@PathVariable("id") Long id, @RequestBody Product prod) {
		var prodModel = productRepository.findById(id).orElseThrow(() -> new RuntimeException());
		updateData(prodModel, prod);
		return prodModel;
	}

	@Operation(summary = "Delete a Product")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		productRepository.deleteById(id);
	}

	private void updateData(Product prodModel, Product prod) {
		prodModel.setProductName(prod.getProductName());
		prodModel.setPrice(prod.getPrice());
	}
}

package br.com.workcode.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.workcode.dto.ProductDto;
import br.com.workcode.model.Product;
import br.com.workcode.service.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product Endpoints")
@RestController
@RequestMapping("/product-service")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;
	
	// Find All Products
	@Operation(summary = "Find All Products")
	@GetMapping
	@CircuitBreaker(name = "findAllCircuit", fallbackMethod = "fallBackFindAll")
	public List<ProductDto> findAllProducts() {

		List<Product> products = productService.findAllProducts();
		
        return products.stream().map((product) -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
	}

	// Find Product by id and make conversion
	@Operation(summary = "Find a Product By your Id and make a conversion by currency")
	@GetMapping("/conversion/{id}")
	@CircuitBreaker(name = "findByIdAndCurrency", fallbackMethod = "FallBackProduct")
	public ResponseEntity<ProductDto> findByIdAndCurrency(@PathVariable Long id, @RequestParam String currency) {
		Product product = productService.findByIdAndDoConversion(id, currency);
		
		// Convert Entity to Dto
		ProductDto productResponse = modelMapper.map(product, ProductDto.class);
		
		return ResponseEntity.ok().body(productResponse);
	}

	// Find Products by catalog
	@Operation(summary = "Filters all products by your catalog")
	@GetMapping("/filter")
	@CircuitBreaker(name = "findAllProdCatalog", fallbackMethod = "fallBackCatalogProd")
	public List<ProductDto> findAllProductsByCatalog(@RequestParam String catalog) {
		
		// Using service to find Products By Catalog
		List<Product> products = productService.findAllProductsByCatalog(catalog);
		
		// Converting List of Products Entity to Products DTO
		return products.stream().map((product) -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
	}

	// Find Product By Id
	@Operation(summary = "Find a Product By your Id with your catalog tax")
	@GetMapping("/item/{id}")
	@CircuitBreaker(name = "findById", fallbackMethod = "fallBackFindById")
	public ResponseEntity<ProductDto> findItemById(@PathVariable Long id) {
		// Find Product Entity with ProductService findProductById
		Product product = productService.findProductById(id);
		
		// Convert Entity to DTO
		ProductDto productResponse = modelMapper.map(product, ProductDto.class);
		
		return ResponseEntity.ok().body(productResponse);
	}

	// FallBack For Find All Products
	public List<Product> fallBackFindAll(Exception e) {
		List<Product> products = productService.findAllProducts();
		for (Product product : products) {
			product.setId(null);
			product.setProductName("Product unavailable");
			product.setCatalog("Catalog unavailable");
			product.setPrice(BigDecimal.valueOf(0.0));
		}
		return products;
	}

	// Fallback for Product find by Id
	public Product fallBackFindById(Long id, Exception e) {
		String prodName = "Product unavailable";
		BigDecimal prodPrice = BigDecimal.valueOf(0.0);
		String catalog = "Catalog unavailable";
		return new Product(null, prodName, catalog, prodPrice);
	}

	// FallBack For Product find by catalog
	public List<Product> fallBackCatalogProd(String catalog, Exception e) {
		List<Product> products = productService.findAllProductsByCatalog(catalog);
		for (Product product : products) {
			product.setId(null);
			product.setProductName("Product unavailable");
			product.setCatalog("Catalog unavailable");
			product.setPrice(BigDecimal.valueOf(0.0));
		}
		return products;
	}

	// FallBack For Product converted with cambio
	public Product FallBackProduct(Long id, String currency, Exception e) {
		String prodName = "Product unavailable";
		BigDecimal prodPrice = BigDecimal.valueOf(0.0);
		String catalog = "Catalog unavailable";
		return new Product(null, prodName, catalog, prodPrice);
	}

	// Update Product
	@Operation(summary = "Update a Product")
	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> update(@PathVariable("id") Long id, @RequestBody Product prod) {
		// Find Product by Id in Entity
		Product prodModel = productService.findProductById(id);
		
		// Make the update Method
		Product prodUpdate = productService.update(id, prodModel);
		
		// Convert Entity to DTO
		ProductDto productResponse = modelMapper.map(prodUpdate, ProductDto.class);
		
		return ResponseEntity.ok().body(productResponse);
	}

	// Delete Product
	@Operation(summary = "Delete a Product")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
}

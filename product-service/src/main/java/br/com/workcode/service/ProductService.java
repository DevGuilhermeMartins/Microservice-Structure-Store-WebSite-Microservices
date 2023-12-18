package br.com.workcode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.workcode.model.Product;
import br.com.workcode.proxy.CambioProxy;
import br.com.workcode.proxy.CatalogProxy;
import br.com.workcode.repository.ProductRepository;
import br.com.workcode.response.Catalog;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CambioProxy cambioProxy;
	
	@Autowired
	private CatalogProxy catalogProxy;
	
	// R - Find All Products
	public List<Product> findAllProducts(){
		List<Product> products = productRepository.findAll();
		for (Product product : products) {
			Catalog catalogs = catalogProxy.getTaxCatalog(product.getCatalog(), product.getPrice());
			product.setPrice(catalogs.getTotalValue());
		}
		return products;
	}
	
	// Find Product By Id and Make a Conversion
	public Product findByIdAndDoConversion(Long id, String currency) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException());
		
		var catalogItem = catalogProxy.getTaxCatalog(product.getCatalog(), product.getPrice());
		product.setPrice(catalogItem.getTotalValue());
		var cambio = cambioProxy.getCambio(product.getPrice(), currency);
		product.setPrice(cambio.getConvertedValue());
		return product;
	}
	
	// Find Products by catalog
	public List<Product> findAllProductsByCatalog(String catalog){
		List<Product> products = productRepository.findProductsByCatalog(catalog);
		for(Product product : products) {
			Catalog productCatalog = catalogProxy.getTaxCatalog(product.getCatalog(), product.getPrice());
			product.setPrice(productCatalog.getTotalValue());
		}
		return products;
	}
	
	// Find Product By Id
	public Product findProductById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Resource not found: " + id));
		var productCatalog = catalogProxy.getTaxCatalog(product.getCatalog(), product.getPrice());
		product.setPrice(productCatalog.getTotalValue());
		return product;
	}
	
	// Update Product
	public Product update(Long id, Product prod) {
		var prodModel = productRepository.findById(id).orElseThrow(() -> new RuntimeException());
		updateData(prodModel, prod);
		return productRepository.save(prodModel);
	}
	
	// Delete product
	public void delete(@PathVariable("id") Long id) {
		productRepository.deleteById(id);
	}

	
	// Method to make the update of data
	private void updateData(Product prodModel, Product prod) {
		prodModel.setProductName(prod.getProductName());
		prodModel.setCatalog(prod.getCatalog());
		prodModel.setPrice(prod.getPrice());
	}
}
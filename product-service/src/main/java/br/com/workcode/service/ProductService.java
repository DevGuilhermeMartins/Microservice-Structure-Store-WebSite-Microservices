package br.com.workcode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.workcode.model.Product;
import br.com.workcode.proxy.CambioProxy;
import br.com.workcode.proxy.CatalogProxy;
import br.com.workcode.repository.ProductRepository;

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
			catalogProxy.getTaxCatalog(product.getCatalog(), product.getPrice());
		}
		return products;
	}
	
	// Find Product By Id and Make a Conversion
	public Product findByIdAndDoConversion(Long id, String currency) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException());
		
		var catalogItem = catalogProxy.getTaxCatalog(product.getCatalog(), product.getPrice());
		var cambio = cambioProxy.getCambio(product.getPrice(), "USD", currency);
		return product;
	}
	
	// Find Products by catalog
	public List<Product> findAllProductsByCatalog(String catalog){
		List<Product> products = productRepository.findProductByCatalog(catalog);
		for(Product product : products) {
			var productCatalog = catalogProxy.getTaxCatalog(product.getCatalog(), product.getPrice());
		}
		return products;
	}
}
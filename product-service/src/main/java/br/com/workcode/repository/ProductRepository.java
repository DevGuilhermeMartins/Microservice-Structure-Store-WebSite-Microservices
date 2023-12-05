package br.com.workcode.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.workcode.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	public List<Product> findProductByCatalog(String catalog);
}

package br.com.workcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.workcode.model.Catalog;

public interface CatalogRepository extends JpaRepository<Catalog, Long>{
		
	public Catalog findByCatalogName(String catalog);
	
}

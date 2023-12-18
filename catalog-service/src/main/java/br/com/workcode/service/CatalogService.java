package br.com.workcode.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.workcode.model.Catalog;
import br.com.workcode.repository.CatalogRepository;

@Service
public class CatalogService {

	@Autowired
	private CatalogRepository catalogRepository;
	
	public Catalog getCatalogAndTax(String catalog, BigDecimal amount) {
		Catalog catalogItem = catalogRepository.findByCatalogName(catalog);
		
		BigDecimal totalValue = amount.add(catalogItem.getCatalogTax());
		catalogItem.setTotalValue(totalValue);
		return catalogItem;
	}
}

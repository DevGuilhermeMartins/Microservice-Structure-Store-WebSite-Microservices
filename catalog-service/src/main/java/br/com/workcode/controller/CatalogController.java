package br.com.workcode.controller;

import java.math.BigDecimal;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.workcode.model.Catalog;
import br.com.workcode.repository.CatalogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Catalog Endpoint")
@RestController
@RequestMapping("catalog-service")
@RequiredArgsConstructor
public class CatalogController {

	private final CatalogRepository catalogRepository;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@PostMapping
	public Catalog save(Catalog catalog) {
		catalogRepository.save(catalog);
		String routingKey = "catalog.v1.catalog-created";
		rabbitTemplate.convertAndSend(routingKey, catalog.getId());
		return catalog;
	}
	
	@Operation(summary = "Take a Catalog and sum the amount with the fixed tax of the catalog")
	@GetMapping
	public Catalog getTaxCatalog(@RequestParam String catalog, @RequestParam BigDecimal amount) {
		var catalogItem = catalogRepository.findByCatalogName(catalog);
		
		var totalValue = amount.add(catalogItem.getCatalogTax());
		catalogItem.setTotalValue(totalValue);
		return catalogItem;
	}
}

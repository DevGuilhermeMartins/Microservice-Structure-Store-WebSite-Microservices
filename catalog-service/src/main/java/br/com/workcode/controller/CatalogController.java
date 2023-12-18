package br.com.workcode.controller;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.workcode.dto.CatalogDto;
import br.com.workcode.model.Catalog;
import br.com.workcode.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Catalog Endpoint")
@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
	
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CatalogService catalogService;
	
	@Operation(summary = "Take a Catalog and sum the amount with the fixed tax of the catalog")
	@GetMapping
	public ResponseEntity<CatalogDto> getTaxCatalog(@RequestParam String catalog, @RequestParam BigDecimal amount) {
		
		Catalog getCatalog = catalogService.getCatalogAndTax(catalog, amount);
		
		// Convert Entity to DTO
		CatalogDto catalogResponse = modelMapper.map(getCatalog, CatalogDto.class);
		
		return ResponseEntity.ok().body(catalogResponse);
	}
}

package br.com.workcode.controller;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.workcode.dto.CambioDto;
import br.com.workcode.model.Cambio;
import br.com.workcode.service.CambioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cambio Service API")
@RestController
@RequestMapping("/cambio-service")
public class CambioController {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CambioService cambioService;
	
	@Operation(summary = "Get a value and makes a conversion by the currency from and to")
	@GetMapping
	public ResponseEntity<CambioDto> getCambio(@RequestParam("amount") BigDecimal amount,
							@RequestParam("from") String from,
							@RequestParam("to") String to) {
		
		// Using getCambio with CambioEntity
		Cambio cambioEntity = cambioService.getCambio(amount, from, to);
		
		// Convert Entity to DTO
		CambioDto cambioResponse = modelMapper.map(cambioEntity, CambioDto.class);
		
		return ResponseEntity.ok().body(cambioResponse);
	}
}

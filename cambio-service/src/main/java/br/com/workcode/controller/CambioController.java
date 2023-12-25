package br.com.workcode.controller;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.workcode.dto.CambioDto;
import br.com.workcode.model.Cambio;
import br.com.workcode.service.CambioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cambio Service API")
@RestController
@RequestMapping("/cambio-service")
public class CambioController {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CambioService cambioService;
	
	@Operation(summary = "Get cambio using amount of conversion and country to convert")
	@GetMapping
	public ResponseEntity<CambioDto> getCambio(@RequestParam("amount") BigDecimal amount,
							@RequestParam("to") String to) {
		
		// Using getCambio with CambioEntity
		Cambio cambioEntity = cambioService.getCambio(amount, to);
		
		// Convert Entity to DTO
		CambioDto cambioResponse = modelMapper.map(cambioEntity, CambioDto.class);
		
		return ResponseEntity.ok().body(cambioResponse);
	}
	
	@Operation(summary = "Create Cambio")
	@PostMapping
	public ResponseEntity<CambioDto> saveCambio(@RequestBody CambioDto cambioDto){
		// Convert Dto to Entity
		Cambio cambioEntity = modelMapper.map(cambioDto, Cambio.class);
		
		// Make the method to create a Cambio
		Cambio cambioCreate = cambioService.saveCambio(cambioEntity);
		
		// Convert Entity to Dto
		CambioDto cambioResponse = modelMapper.map(cambioCreate, CambioDto.class);
		
		return new ResponseEntity<CambioDto>(cambioResponse, HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update Cambio")
	@PutMapping("/{to}")
	public ResponseEntity<CambioDto> updateCambio(@PathVariable String to, @RequestBody Cambio cambio){
		// Find Cambio By id
		Cambio cambioEntity = cambioService.findByTo(to);
		
		// Make the update Method
		Cambio cambioUpdate = cambioService.updateCambio(to, cambioEntity);
		
		// Convert Entity to Dto
		CambioDto cambioResponse = modelMapper.map(cambioUpdate, CambioDto.class);
		
		return ResponseEntity.ok().body(cambioResponse);
	}
	
	// Delete Cambio
	@Operation(summary = "Delete Cambio")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		cambioService.deleteCambio(id);
		return ResponseEntity.noContent().build();
	}
}

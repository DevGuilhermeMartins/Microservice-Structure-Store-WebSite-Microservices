package br.com.workcode.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import br.com.workcode.model.Cambio;
import br.com.workcode.repository.CambioRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CambioService {

	
	private final CambioRepository cambioRepository;
	
	
	// Get cambio using amount of conversion and country to convert
	public Cambio getCambio(BigDecimal amount, String to) {
		
		Cambio cambio = cambioRepository.findByTo(to);
		BigDecimal conversionFactor = cambio.getConversionFactor();
		BigDecimal convertedValue = conversionFactor.multiply(amount);
		cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
		return cambio;
	}
	
	// Get Cambio by country to
	public Cambio findByTo(String to) {
		Cambio cambioEntity = findByTo(to);
		return cambioEntity;
	}
	
	// Create Cambio
	public Cambio saveCambio(Cambio cambio) {
		return cambioRepository.save(cambio);
	}
	
	// Update Cambio
	public Cambio updateCambio(String to, Cambio updateCambio) {
		Cambio cambioEntity = cambioRepository.findByTo(to);
		updateCambio(cambioEntity, updateCambio);
		return cambioRepository.save(cambioEntity);
	}
	
	// Delete Cambio
	public void deleteCambio(Long id) {
		Cambio cambioEntity = cambioRepository.findById(id).orElseThrow(() -> new RuntimeException("Resource Not Found: " + id));
		cambioRepository.delete(cambioEntity);
	}

	private void updateCambio(Cambio cambioEntity, Cambio updateCambio) {
		cambioEntity.setTo(updateCambio.getTo());
		cambioEntity.setConversionFactor(updateCambio.getConversionFactor());
	}
	

}

package br.com.workcode.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.workcode.model.Cambio;
import br.com.workcode.repository.CambioRepository;

@Service
public class CambioService {

	@Autowired
	private CambioRepository cambioRepository;
	
	
	public Cambio getCambio(BigDecimal amount, String to) {
		
		Cambio cambio = cambioRepository.findByTo(to);
		BigDecimal conversionFactor = cambio.getConversionFactor();
		BigDecimal convertedValue = conversionFactor.multiply(amount);
		cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
		return cambio;
	}
}

package br.com.workcode.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.workcode.model.Cambio;
import br.com.workcode.repository.CambioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cambio Service API")
@RestController
@RequestMapping("/cambio-service")
@RequiredArgsConstructor
public class CambioController {

	private final CambioRepository cambioRepository;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@PostMapping
	public Cambio saveCambio(Cambio cambio) {
		cambioRepository.save(cambio);
		String routingKey = "cambio.v1.cambio-created";
		rabbitTemplate.convertAndSend(routingKey, cambio.getId());
		return cambio;
	}
	
	@Operation(summary = "Get a value and make a conversion by the currency from and to")
	@GetMapping
	public Cambio getCambio(@RequestParam("amount") BigDecimal amount,
							@RequestParam("from") String from,
							@RequestParam("to") String to,
							@RequestParam("tax") BigDecimal tax) {
		
		var cambio = cambioRepository.findByFromAndTo(from, to);
		
		if(cambio == null) new RuntimeException();
		BigDecimal conversionFactor = cambio.getConversionFactor();
		BigDecimal convertedValue = conversionFactor.multiply(amount);
		convertedValue.add(tax);
		cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
		return cambio;
	}
}

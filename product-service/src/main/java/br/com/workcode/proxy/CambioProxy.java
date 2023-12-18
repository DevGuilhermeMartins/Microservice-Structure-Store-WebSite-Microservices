package br.com.workcode.proxy;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.workcode.response.Cambio;


@FeignClient(name = "cambio-service")
public interface CambioProxy {

	@GetMapping(value = "/cambio-service")
	public Cambio getCambio(@RequestParam("amount") BigDecimal amount,
							@RequestParam("to") String to);
}

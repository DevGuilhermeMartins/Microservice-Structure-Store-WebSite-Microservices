package br.com.workcode.proxy;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "cambio-service")
public interface CambioProxy {

	@GetMapping(value = "/cambio-service")
	ResponseEntity getCambio(@RequestParam("amount") BigDecimal amount,
							@RequestParam("from") String from,
							@RequestParam("to") String to);
}

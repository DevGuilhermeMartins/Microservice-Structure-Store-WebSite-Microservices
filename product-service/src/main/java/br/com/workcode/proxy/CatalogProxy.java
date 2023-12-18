package br.com.workcode.proxy;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "catalog-service")
public interface CatalogProxy {

	@GetMapping(value = "/catalog-service")
	ResponseEntity getTaxCatalog(@RequestParam String catalog,
								 @RequestParam BigDecimal amount);
	
}

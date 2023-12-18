package br.com.workcode.proxy;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.workcode.response.Catalog;

@FeignClient(name = "catalog-service")
public interface CatalogProxy {

	@GetMapping(value = "/catalog-service")
	public Catalog getTaxCatalog(@RequestParam String catalog,
								 @RequestParam BigDecimal amount);
	
}

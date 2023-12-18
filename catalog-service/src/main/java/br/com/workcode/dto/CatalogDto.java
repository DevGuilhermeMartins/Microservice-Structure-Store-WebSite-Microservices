package br.com.workcode.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogDto {

	private String catalogName;
	private BigDecimal catalogTax;
	private BigDecimal totalValue;

}

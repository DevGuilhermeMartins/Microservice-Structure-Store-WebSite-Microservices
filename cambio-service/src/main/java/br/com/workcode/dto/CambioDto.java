package br.com.workcode.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CambioDto {

	private String from;
	private String to;
	private BigDecimal conversionFactor;
	private BigDecimal convertedValue;
}

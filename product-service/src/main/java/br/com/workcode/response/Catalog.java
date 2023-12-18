package br.com.workcode.response;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catalog  implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String catalogName;
	private BigDecimal catalogTax;
	private BigDecimal totalValue;
}

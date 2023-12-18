package br.com.workcode.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "cambio")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Cambio implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "to_currency", nullable = false, length = 3)
	private String to;
	@Column(name = "conversion_factor", nullable = false)
	private BigDecimal conversionFactor;
	
	private BigDecimal convertedValue;
	
	
}

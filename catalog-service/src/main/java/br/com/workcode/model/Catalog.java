package br.com.workcode.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Catalog implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "catalog_name", nullable = false)
	private String catalogName;
	
	@Column(nullable = false)
	private BigDecimal catalogTax;
	
	@Transient
	private BigDecimal totalValue;
}

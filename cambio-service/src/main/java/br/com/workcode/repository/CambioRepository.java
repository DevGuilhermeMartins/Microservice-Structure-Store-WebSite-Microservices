package br.com.workcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.workcode.model.Cambio;

public interface CambioRepository extends JpaRepository<Cambio, Long>{

	public Cambio findByTo(String to);
}

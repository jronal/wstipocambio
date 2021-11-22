package com.bcp.wstipocambio.repository;

import com.bcp.wstipocambio.entity.TipoCambio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoCambioRepository  extends JpaRepository<TipoCambio, Integer> {
    Optional<TipoCambio> findByMonedaOrigenAndMonedaDestino(String monedaOrigen, String monedaDestino);

}

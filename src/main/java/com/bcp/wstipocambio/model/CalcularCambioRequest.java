package com.bcp.wstipocambio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalcularCambioRequest {
    private Double monto;

    private String monedaOrigen;

    private String monedaDestino;

}

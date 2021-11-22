package com.bcp.wstipocambio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoCambioRequest {
    private String monedaOrigen;
    private String monedaDestino;
    private Double cotizacion;
}

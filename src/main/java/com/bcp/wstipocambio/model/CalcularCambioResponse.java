package com.bcp.wstipocambio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalcularCambioResponse {
    Double monto;
    Double montoTipoCambio;
    String monedaOrigen;
    String monedaDestino;

    Double cotizacion;
}

package com.bcp.wstipocambio.controller;

import com.bcp.wstipocambio.entity.TipoCambio;
import com.bcp.wstipocambio.model.BaseWebResponse;
import com.bcp.wstipocambio.model.CalcularCambioRequest;
import com.bcp.wstipocambio.model.CalcularCambioResponse;
import com.bcp.wstipocambio.model.TipoCambioRequest;
import com.bcp.wstipocambio.service.JWTService;
import com.bcp.wstipocambio.service.TipoCambioService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static io.jsonwebtoken.Claims.AUDIENCE;


@RestController
@RequestMapping("/api/v1/tipocambio")
public class TipoCambioController {

    @Autowired
    TipoCambioService tipoCambioService;

    @Autowired
    JWTService jwtService;

    @GetMapping(value = "/{monto}/{monedaOrigen}/{monedaDestino}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Single<ResponseEntity<BaseWebResponse<CalcularCambioResponse>>> calcular(
            @PathVariable(value = "monto") Double monto,
            @PathVariable(value = "monedaOrigen") String monedaOrigen,
            @PathVariable(value = "monedaDestino") String monedaDestino
    ) {
        CalcularCambioRequest request = CalcularCambioRequest.builder()
                .monto(monto)
                .monedaOrigen(monedaOrigen)
                .monedaDestino(monedaDestino)
                .build();

        return tipoCambioService.calcular(request)
                .subscribeOn(Schedulers.io())
                .map(calcularCambioResponse -> ResponseEntity.ok(BaseWebResponse.successWithData(calcularCambioResponse)));
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Single<ResponseEntity<BaseWebResponse>> guardar(@RequestBody TipoCambioRequest tipoCambioRequest) {
        return tipoCambioService.updateTipoCambio(tipoCambioRequest)
                .subscribeOn(Schedulers.io())
                .toSingle(() -> ResponseEntity.ok(null));
    }


    @GetMapping(value = "/jwt/{dni}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String calcular(
            @PathVariable(value = "dni") String dni
    ) {
        return jwtService.getToken(dni, AUDIENCE);
    }


}

package com.bcp.wstipocambio.service;

import com.bcp.wstipocambio.entity.TipoCambio;
import com.bcp.wstipocambio.model.CalcularCambioRequest;
import com.bcp.wstipocambio.model.CalcularCambioResponse;
import com.bcp.wstipocambio.model.TipoCambioRequest;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.springframework.stereotype.Service;

@Service
public interface TipoCambioService {

    Completable updateTipoCambio(TipoCambioRequest tipoCambioRequest);


    Single<CalcularCambioResponse> calcular(CalcularCambioRequest tipoCambioRequest);
}

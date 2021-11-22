package com.bcp.wstipocambio.service.impl;

import com.bcp.wstipocambio.entity.TipoCambio;
import com.bcp.wstipocambio.exception.ApiException;
import com.bcp.wstipocambio.model.CalcularCambioRequest;
import com.bcp.wstipocambio.model.CalcularCambioResponse;
import com.bcp.wstipocambio.model.TipoCambioRequest;
import com.bcp.wstipocambio.repository.TipoCambioRepository;
import com.bcp.wstipocambio.service.TipoCambioService;
import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Optional;

@Service
@Slf4j
public class TipoCambioServiceImpl implements TipoCambioService {

    private static final String NOT_FOUND_TIPO_CAMBIO_EXCEPTION = "not_found.tipo_cambio.exception";
    private static final String TIPO_CAMBIO_EXCEPTION = "tipo_cambio.exception";

    @Autowired
    TipoCambioRepository tipoCambioRepository;

    @Override
    public Completable updateTipoCambio(TipoCambioRequest tipoCambioRequest) {
        log.info("{}", tipoCambioRequest);

        return Completable.create(completableSubscriber -> {
            Optional<TipoCambio> tipoCambio = tipoCambioRepository.findByMonedaOrigenAndMonedaDestino(
                    tipoCambioRequest.getMonedaOrigen(), tipoCambioRequest.getMonedaDestino()
            );

            if (tipoCambio.isPresent()) {
                tipoCambio.get().setCotizacion(tipoCambioRequest.getCotizacion());
                tipoCambioRepository.save(tipoCambio.get());
                completableSubscriber.onComplete();
            } else {
                TipoCambio tipoCambioNew = TipoCambio.builder()
                        .monedaOrigen(tipoCambioRequest.getMonedaOrigen())
                        .monedaDestino(tipoCambioRequest.getMonedaDestino())
                        .cotizacion(tipoCambioRequest.getCotizacion())
                        .build();
                tipoCambioRepository.save(tipoCambioNew);
                completableSubscriber.onComplete();
//                completableSubscriber.onError(new EntityNotFoundException());
            }
        });
    }

    @Override
    public Single<CalcularCambioResponse> calcular(CalcularCambioRequest calcularCambioRequest) {
        log.info("Inicia calcular");
        log.info("{}", calcularCambioRequest);
        return Single.create(singleSubscriber -> {
            Optional<TipoCambio> tipoCambio = tipoCambioRepository.findByMonedaOrigenAndMonedaDestino(
                    calcularCambioRequest.getMonedaOrigen(), calcularCambioRequest.getMonedaDestino()
            );

            if (!tipoCambio.isPresent())
                singleSubscriber.onError(new ApiException(NOT_FOUND_TIPO_CAMBIO_EXCEPTION, HttpStatus.NOT_FOUND));
            else {
                Double result = formatTipoCambio(calcularCambioRequest.getMonto() * tipoCambio.get().getCotizacion());

                // TODO redondear.. result
                CalcularCambioResponse calcularCambioResponse = CalcularCambioResponse.builder()
                        .monto(calcularCambioRequest.getMonto())
                        .montoTipoCambio(result)
                        .monedaOrigen(calcularCambioRequest.getMonedaOrigen())
                        .monedaDestino(calcularCambioRequest.getMonedaDestino())
                        .cotizacion(tipoCambio.get().getCotizacion())
                        .build();
                singleSubscriber.onSuccess(calcularCambioResponse);
                log.info("{}", calcularCambioResponse);
                log.info("Fin calcular");
            }
        });
    }


    // TODO
    private Double formatTipoCambio(Double monto) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(monto));
    }

    // TODO
    private boolean validMontoTipoCambio(Double monto) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        try {
            df.parse(monto.toString());
            return true;
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

}

package com.bcp.wstipocambio.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {


    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.code = httpStatus.value();
    }

    @Override
    public String toString() {
        return message;
    }
}

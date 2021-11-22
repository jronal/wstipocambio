package com.bcp.wstipocambio.exception;

public class TokenInvalidHeaderException extends RuntimeException {
    public TokenInvalidHeaderException() {
    }

    public TokenInvalidHeaderException(String message) {
        super(message);
    }

    public TokenInvalidHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenInvalidHeaderException(Throwable cause) {
        super(cause);
    }
}

package br.com.foursales.product_service.domain.exception;


import java.io.Serial;

public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5526158149569980316L;

    public BusinessException(String message) {
        super(message);
    }
}
package br.com.foursales.product_service.domain.exception;

import java.io.Serial;

public class MessageProcessingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4346315546639785104L;

    public MessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageProcessingException(String message) {
        super(message);
    }
}
package br.com.foursales.product_service.domain.exception;

import br.com.foursales.product_service.application.event.dto.error.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDetail> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrorDetail errorResponse = new ErrorDetail(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorDetail> handleSecurityException(SecurityException ex) {
        ErrorDetail errorResponse = new ErrorDetail(
                String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                HttpStatus.UNAUTHORIZED.name(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}

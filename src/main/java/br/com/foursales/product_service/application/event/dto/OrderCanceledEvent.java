package br.com.foursales.product_service.application.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCanceledEvent {
    private UUID orderId;
    private String reason;
}

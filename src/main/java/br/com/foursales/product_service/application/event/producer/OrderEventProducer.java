package br.com.foursales.product_service.application.event.producer;

import br.com.foursales.product_service.application.event.dto.OrderCanceledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCanceledEvent> kafkaTemplate;
    private static final String TOPIC = "order.canceled";

    public void publishOrderCanceledEvent(OrderCanceledEvent event) {
        log.info("Publicando evento de cancelamento de pedido: {}", event);
        kafkaTemplate.send(TOPIC, event);
    }
}

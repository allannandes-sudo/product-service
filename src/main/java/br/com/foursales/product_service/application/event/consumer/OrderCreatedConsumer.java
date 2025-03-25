package br.com.foursales.product_service.application.event.consumer;

import br.com.foursales.product_service.application.event.dto.OrderCreatedEvent;
import br.com.foursales.product_service.domain.exception.MessageProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final ObjectMapper objectMapper;
    @KafkaListener(
            topics = "order.created",
            groupId = "product-service-group"
    )
    @Transactional
    public void consumeOrderCreated(@Payload String message) {
        log.info("Recebido evento de pedido criado: {}", message);
        try {
            var dto = objectMapper.readValue(message, OrderCreatedEvent.class);
            log.info("Recebido evento de pedido criado: {}", dto.getItems());
        } catch (JsonProcessingException e) {
            log.error("Erro ao processar mensagem JSON: {}", message, e);
            throw new MessageProcessingException("Erro ao processar mensagem JSON", e);
        } catch (Exception e) {
            log.error("Erro ao processar pedido: {}", e.getMessage(), e);
            throw new MessageProcessingException("Erro ao processar pedido", e);
        }
    }
}

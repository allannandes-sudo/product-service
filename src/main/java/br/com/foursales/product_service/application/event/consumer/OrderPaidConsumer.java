package br.com.foursales.product_service.application.event.consumer;

import br.com.foursales.product_service.application.event.dto.OrderCanceledEvent;
import br.com.foursales.product_service.application.event.dto.OrderItemEvent;
import br.com.foursales.product_service.application.event.dto.OrderPaidEvent;
import br.com.foursales.product_service.application.event.producer.OrderEventProducer;
import br.com.foursales.product_service.application.service.ProductService;
import br.com.foursales.product_service.domain.exception.MessageProcessingException;
import br.com.foursales.product_service.domain.model.ProductResponse;
import br.com.foursales.product_service.domain.model.ProductUpdateRequest;
import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import br.com.foursales.product_service.infrastructure.persistence.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidConsumer {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final OrderEventProducer orderEventProducer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order.paid", groupId = "product-service-group")
    public void consumeOrderPaid(@Payload String message) {
        log.info("Recebido evento de pagamento de pedido: {}", message);
        try {
            var dto = objectMapper.readValue(message, OrderPaidEvent.class);
            log.info("Recebido evento de pedido criado: {}", dto.getItems());
            extracted(dto);

        } catch (JsonProcessingException e) {
            log.error("Erro ao processar mensagem JSON: {}", message, e);
            throw new MessageProcessingException("Erro ao processar mensagem JSON", e);
        } catch (Exception e) {
            log.error("Erro ao processar pedido: {}", e.getMessage(), e);
            throw new MessageProcessingException("Erro ao processar pedido", e);
        }

    }

    private void extracted(OrderPaidEvent event) {
        boolean insufficientStock = false;

        for (OrderItemEvent item : event.getItems()) {
            Optional<ProductEntity> productOpt = productRepository.findByProductId(item.getProductId());

            if (productOpt.isPresent()) {
                ProductEntity product = productOpt.get();
                int newStock = product.getStock() - item.getQuantity();

                if (newStock < 0) {
                    log.warn("Estoque insuficiente para o produto ID: {}", product.getId());
                    insufficientStock = true;
                    break;
                }
            } else {
                log.warn("Produto não encontrado: ID {}", item.getProductId());
            }
        }

        if (insufficientStock) {
            log.info("Cancelando pedido {} por falta de estoque.", event.getOrderId());
            orderEventProducer.publishOrderCanceledEvent(
                    new OrderCanceledEvent(event.getOrderId(), "Produto sem estoque")
            );
        } else {
            for (OrderItemEvent item : event.getItems()) {
                ProductEntity product = productRepository.findByProductId(item.getProductId()).orElseThrow();

                // Criando o objeto ProductUpdateRequest
                ProductUpdateRequest request = new ProductUpdateRequest();
                request.setCategory(product.getCategory());
                request.setPrice(product.getPrice());
                request.setStock(product.getStock() - item.getQuantity());

                // Atualiza via método já existente
                ProductResponse response = productService.updateProductByName(product.getName(), request);
                if (response != null) {
                    log.info("Estoque atualizado para o produto {}: {}", product.getId(), response.getStock());
                } else {
                    log.warn("Falha ao atualizar estoque para o produto {}.", product.getId());
                }
            }
        }
    }
}

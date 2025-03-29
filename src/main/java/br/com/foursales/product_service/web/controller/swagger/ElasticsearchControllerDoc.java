package br.com.foursales.product_service.web.controller.swagger;

import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import br.com.foursales.product_service.web.annotations.DefaultSwaggerMessage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Elasticsearch", description = "Buscar de produtos!")
public interface ElasticsearchControllerDoc {

    @GetMapping("/search")
    @DefaultSwaggerMessage
    ResponseEntity<List<ProductDocument>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    );


}

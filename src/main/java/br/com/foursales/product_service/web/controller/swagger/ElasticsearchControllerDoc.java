package br.com.foursales.product_service.web.controller.swagger;

import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import br.com.foursales.product_service.web.annotations.DefaultSwaggerMessage;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Elasticsearch", description = "Buscar de produtos!")
public interface ElasticsearchControllerDoc {

    @DefaultSwaggerMessage
    @GetMapping("/search")
    List<ProductDocument> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) String category
    ) ;


}

package br.com.foursales.product_service.web.controller;

import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import br.com.foursales.product_service.infrastructure.search.service.ProductSearchService;
import br.com.foursales.product_service.web.controller.swagger.ElasticsearchControllerDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products/elasticsearch")
@RequiredArgsConstructor
public class ElasticsearchController implements ElasticsearchControllerDoc {

    private final ProductSearchService productSearchService;

    @GetMapping("/search")
    public ResponseEntity<List<ProductDocument>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        try {
            List<ProductDocument> products = productSearchService.searchProducts(name, category, minPrice, maxPrice);

            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(products);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}

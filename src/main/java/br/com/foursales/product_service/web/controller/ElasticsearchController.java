package br.com.foursales.product_service.web.controller;

import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import br.com.foursales.product_service.infrastructure.search.service.ProductSearchService;
import br.com.foursales.product_service.web.controller.swagger.ElasticsearchControllerDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/elasticsearch")
@RequiredArgsConstructor
public class ElasticsearchController implements ElasticsearchControllerDoc {

    private final ProductSearchService productSearchService;

    @GetMapping("/search")
    public List<ProductDocument> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) String category
    ) {

        return productSearchService.searchByNamePriceAndCategory(name, min, max, category);
    }
}

package br.com.foursales.product_service.web.controller;

import br.com.foursales.product_service.application.service.ProductService;
import br.com.foursales.product_service.domain.model.ProductCreateResponse;
import br.com.foursales.product_service.domain.model.ProductRequest;
import br.com.foursales.product_service.domain.model.ProductResponse;
import br.com.foursales.product_service.domain.model.ProductStockResponse;
import br.com.foursales.product_service.domain.model.ProductUpdateRequest;
import br.com.foursales.product_service.web.controller.swagger.ProductControllerDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController implements ProductControllerDoc {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID productId) {
        return productService.getProductById(productId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductCreateResponse> createProduct(@RequestBody ProductRequest productRequest) {
        var response = productService.createProduct(productRequest);
        URI location = URI.create("/api/products/" + response.getProductResponse().getId());
        log.info("Criando produto :{} , no PATH: {}, ", response, location);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update-by-name/{name}")
    public ResponseEntity<ProductResponse> updateProductByName(
            @PathVariable String name,
            @RequestBody ProductUpdateRequest productUpdateRequest
    ) {
        ProductResponse updatedProduct = productService.updateProductByName(name, productUpdateRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/stock-check")
    public ResponseEntity<Map<UUID, ProductStockResponse>> checkStock(@RequestParam List<UUID> productIds) {
        Map<UUID, ProductStockResponse> stockStatus = productService.checkStock(productIds);
        return ResponseEntity.ok(stockStatus);
    }

}

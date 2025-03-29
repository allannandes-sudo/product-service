package br.com.foursales.product_service.web.controller.swagger;

import br.com.foursales.product_service.domain.model.ProductCreateResponse;
import br.com.foursales.product_service.domain.model.ProductRequest;
import br.com.foursales.product_service.domain.model.ProductResponse;
import br.com.foursales.product_service.domain.model.ProductStockResponse;
import br.com.foursales.product_service.domain.model.ProductUpdateRequest;
import br.com.foursales.product_service.web.annotations.DefaultSwaggerMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Produto", description = "Endpoints para cadastrado de protudo!")
public interface ProductControllerDoc {

    @DefaultSwaggerMessage
    @GetMapping
    @Operation(summary = "Lista de todos produtos!!")
    ResponseEntity<List<ProductResponse>> getAllProducts();


    @DefaultSwaggerMessage
    @GetMapping("/{productId}")
    @Operation(summary = "Busca de produto por productId!!")
    ResponseEntity<ProductResponse> getProductById(@PathVariable UUID productId);


    @DefaultSwaggerMessage
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cadastrar produto!!")
    ResponseEntity<ProductCreateResponse> createProduct(@RequestBody ProductRequest productRequest);

    @DefaultSwaggerMessage
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar produto pelo productId!!")
    ResponseEntity<Void> deleteProduct(@PathVariable UUID productId);

    @DefaultSwaggerMessage
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update-by-name/{name}")
    @Operation(summary = "Atualização do produto!!")
    ResponseEntity<ProductResponse> updateProductByName(
            @PathVariable String name,
            @RequestBody ProductUpdateRequest productUpdateRequest
    );

    @DefaultSwaggerMessage
    @GetMapping("/stock-check")
    @Operation(summary = "Validação dos estoque!!")
    ResponseEntity<Map<UUID, ProductStockResponse>> checkStock(@RequestParam List<UUID> productIds);

}

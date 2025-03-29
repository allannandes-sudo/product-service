package br.com.foursales.product_service.application.service;

import br.com.foursales.product_service.domain.model.ProductCreateResponse;
import br.com.foursales.product_service.domain.model.ProductRequest;
import br.com.foursales.product_service.domain.model.ProductResponse;
import br.com.foursales.product_service.domain.model.ProductStockResponse;
import br.com.foursales.product_service.domain.model.ProductUpdateRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    List<ProductResponse> getAllProducts();
    ProductCreateResponse createProduct(ProductRequest productRequest) ;
    void deleteProduct(UUID productId);
    ProductResponse updateProductByName(String name, ProductUpdateRequest productUpdateRequest);
    Optional<ProductResponse> getProductById(UUID productId);
    Map<UUID, ProductStockResponse> checkStock(List<UUID> productIds);


}

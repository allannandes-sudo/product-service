package br.com.foursales.product_service.application.service;

import br.com.foursales.product_service.domain.model.ProductCreateResponse;
import br.com.foursales.product_service.domain.model.ProductRequest;
import br.com.foursales.product_service.domain.model.ProductResponse;
import br.com.foursales.product_service.domain.model.ProductUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    Optional<ProductResponse> getProductById(Long id) ;
    ProductCreateResponse createProduct(ProductRequest productRequest) ;
    void deleteProduct(Long id);
    ProductResponse updateProductByName(String name, ProductUpdateRequest productUpdateRequest);
    List<ProductResponse> searchProducts(String query);


}

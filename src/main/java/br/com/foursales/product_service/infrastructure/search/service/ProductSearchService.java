package br.com.foursales.product_service.infrastructure.search.service;


import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import br.com.foursales.product_service.infrastructure.search.ProductDocument;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductSearchService {


    void indexProduct(ProductEntity product);

    void deleteProduct(UUID productId);

    List<ProductDocument> searchProducts(String name, String category, Double minPrice, Double maxPrice) throws IOException;


}
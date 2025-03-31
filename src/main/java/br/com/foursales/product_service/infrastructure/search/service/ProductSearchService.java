package br.com.foursales.product_service.infrastructure.search.service;


import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import br.com.foursales.product_service.infrastructure.search.ProductDocument;

import java.util.List;
import java.util.UUID;

public interface ProductSearchService {


    void indexProduct(ProductEntity product);

    void deleteProduct(UUID productId);

    List<ProductDocument> searchByNamePriceAndCategory(
            String name,
            Double minPrice,
            Double maxPrice,
            String category
    );
}
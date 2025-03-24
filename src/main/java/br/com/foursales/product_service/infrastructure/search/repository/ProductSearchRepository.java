package br.com.foursales.product_service.infrastructure.search.repository;

import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findByNameContainingOrCategoryContaining(String name, String category);
}

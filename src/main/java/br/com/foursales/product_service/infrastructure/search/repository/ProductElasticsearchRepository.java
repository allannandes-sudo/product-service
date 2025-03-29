package br.com.foursales.product_service.infrastructure.search.repository;

import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, String> {
}

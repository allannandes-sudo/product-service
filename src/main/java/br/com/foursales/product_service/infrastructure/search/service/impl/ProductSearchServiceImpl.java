package br.com.foursales.product_service.infrastructure.search.service.impl;


import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import br.com.foursales.product_service.infrastructure.search.mapper.ProductDocumentMapper;
import br.com.foursales.product_service.infrastructure.search.repository.ProductElasticsearchRepository;
import br.com.foursales.product_service.infrastructure.search.service.ProductSearchService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {

    private static final String PRICE_FIELD = "price";

    private final ProductDocumentMapper productDocumentMapper;
    private final ProductElasticsearchRepository elasticsearchRepository;
    private final ElasticsearchClient elasticsearchClient;


    public void indexProduct(ProductEntity product) {
        ProductDocument document = productDocumentMapper.toDocument(product);
        try {
            elasticsearchRepository.save(document);
        } catch (Exception e) {

            log.error("Error saving document to Elasticsearch: " + e.getMessage());
            e.printStackTrace();

        }
    }

    public void deleteProduct(UUID productId) {

        elasticsearchRepository.deleteById(String.valueOf(productId));
    }


    public List<ProductDocument> searchProducts(
            String name,
            String category,
            Double minPrice,
            Double maxPrice) throws IOException {
        List<Query> mustQueries = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            mustQueries.add(Query.of(q -> q.match(m -> m
                    .field("name")
                    .query(name)
                    .fuzziness("AUTO")
            )));
        }

        if (category != null && !category.isBlank()) {
            mustQueries.add(Query.of(q -> q.term(t -> t
                    .field("category")
                    .value(category)
            )));
        }
//        // Filtrando faixa de preço (baseado na documentação oficial)
//        if (minPrice != null || maxPrice != null) {
//            mustQueries.add(Query.of(q -> q.range(r -> {
//                r.field(PRICE_FIELD); // Definição correta do campo de preço
//                if (minPrice != null) r.gte(JsonData.of(minPrice)); // >= minPrice
//                if (maxPrice != null) r.lte(JsonData.of(maxPrice)); // <= maxPrice
//                return r;
//            })));
//        }
//
//        // Filtrando apenas produtos com estoque maior que 0
//        mustQueries.add(Query.of(q -> q.range(r -> r
//                .field("stockQuantity")
//                .gt(JsonData.of(0)) // Apenas produtos disponíveis
//        )));

        //  Construindo a query booleana
        Query boolQuery = Query.of(q -> q.bool(BoolQuery.of(b -> b.must(mustQueries))));

        //  Executando a consulta no Elasticsearch
        SearchResponse<ProductDocument> response = elasticsearchClient.search(s -> s
                        .index("products")
                        .query(boolQuery),
                ProductDocument.class);

        //  Processando os resultados
        List<ProductDocument> products = new ArrayList<>();
        for (Hit<ProductDocument> hit : response.hits().hits()) {
            if (hit.source() != null) {
                products.add(hit.source());
            }
        }

        return products;
    }


}
package br.com.foursales.product_service.infrastructure.search.repository;

import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, String> {




    @Query("{\"bool\": { " +
            "\"must\": [{ \"match_all\": {} }], " + // Retorna tudo, mas será filtrado
            "\"filter\": [ " +
            "{ \"bool\": { \"should\": [ " +
            "{ \"fuzzy\": {\"name\": {\"value\": \"?0\", \"fuzziness\": \"AUTO\"}}} " +
            "], \"minimum_should_match\": 1 } }, " +  // Garante que só aplica se name for passado
            "{ \"bool\": { \"should\": [ " +
            "{ \"range\": {\"price\": {\"gte\": ?1, \"lte\": ?2}}}, " + // Faixa de preço opcional
            "{ \"match_all\": {}} " + // Caso a faixa de preço não seja especificada, inclui match_all
            "]}}," +
            "{ \"bool\": { \"should\": [ " +
            "{ \"term\": {\"category\": \"?3\"}}, " + // Categoria opcional
            "{ \"match_all\": {}} " + // Caso a categoria não seja especificada, inclui match_all
            "]}}" +
            "]}}")
    List<ProductDocument> searchByNamePriceAndCategory(
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("category") String category
    );




}

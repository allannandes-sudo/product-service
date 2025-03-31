package br.com.foursales.product_service.infrastructure.search.service.impl;


import br.com.foursales.product_service.domain.exception.BusinessException;
import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import br.com.foursales.product_service.infrastructure.search.mapper.ProductDocumentMapper;
import br.com.foursales.product_service.infrastructure.search.repository.ProductElasticsearchRepository;
import br.com.foursales.product_service.infrastructure.search.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ProductDocumentMapper productDocumentMapper;
    private final ProductElasticsearchRepository elasticsearchRepository;


    public void indexProduct(ProductEntity product) {
        ProductDocument document = productDocumentMapper.toDocument(product);
        try {
            elasticsearchRepository.save(document);
        } catch (Exception e) {
            log.error("Error saving document to Elasticsearch: {}", e.getMessage());
        }
    }

    public void deleteProduct(UUID productId) {
        try {
            elasticsearchRepository.deleteById(String.valueOf(productId));
        } catch (Exception e) {
            log.error("Error delete document to Elasticsearch: {}", e.getMessage());
        }
    }


    public List<ProductDocument> searchByNamePriceAndCategory(
            String name,
            Double minPrice,
            Double maxPrice,
            String category
    ) {
        // Verificar se todos os filtros estão vazios ou não preenchidos
        if ((name == null || name.isBlank()) && minPrice == null && maxPrice == null && (category == null || category.isBlank())) {
            throw new BusinessException("Pelo menos um filtro deve ser preenchido.");
        }

        // Atribuir valores padrão se necessário
        String searchName = (name != null) ? name : "";
        Double searchMin = (minPrice != null) ? minPrice : 0.0;
        Double searchMax = (maxPrice != null) ? maxPrice : Double.MAX_VALUE;
        String searchCategory = (category != null) ? category : "";

        return elasticsearchRepository.searchByNamePriceAndCategory(searchName, searchMin, searchMax, searchCategory);
    }





}
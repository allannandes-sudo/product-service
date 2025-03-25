package br.com.foursales.product_service.infrastructure.search.service;

import br.com.foursales.product_service.domain.model.ProductResponse;
import br.com.foursales.product_service.infrastructure.persistence.repository.entity.ProductEntity;
import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import br.com.foursales.product_service.infrastructure.search.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchRepository searchRepository;

    public void indexProduct(ProductEntity product) {
        ProductDocument document = new ProductDocument(
                product.getId().toString(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock()
        );
        searchRepository.save(document);
    }

    public List<ProductResponse> searchProducts(String query) {
        List<ProductDocument> results = searchRepository.findByNameContainingOrCategoryContaining(query, query);
        return results.stream()
                .map(doc -> new ProductResponse(
                        doc.getId() != null ? Long.parseLong(doc.getId()) : null,
                        doc.getName(),
                        doc.getCategory(),
                        doc.getPrice(),
                        doc.getStock()
                ))
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long id) {
        searchRepository.deleteById(String.valueOf(id));
    }

}
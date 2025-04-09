package br.com.foursales.product_service.application.service.impl;

import br.com.foursales.product_service.application.service.ProductService;
import br.com.foursales.product_service.domain.exception.ProductNotFoundException;
import br.com.foursales.product_service.domain.model.ProductCreateResponse;
import br.com.foursales.product_service.domain.model.ProductRequest;
import br.com.foursales.product_service.domain.model.ProductResponse;
import br.com.foursales.product_service.domain.model.ProductStockResponse;
import br.com.foursales.product_service.domain.model.ProductUpdateRequest;
import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import br.com.foursales.product_service.infrastructure.persistence.mapper.ProductMapper;
import br.com.foursales.product_service.infrastructure.persistence.repository.ProductRepository;
import br.com.foursales.product_service.infrastructure.search.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper productMapper;
    private final ProductSearchService productSearchService;

    @Override
    public List<ProductResponse> getAllProducts() {
        return repository.findAll().stream()
                .filter(product -> product.getStock() > 0)
                .map(productMapper::toDomain)
                .collect(toList());
    }

    @Override
    public Optional<ProductResponse> getProductById(UUID productId) {
        return repository.findByProductId(productId)
                .filter(product -> product.getStock() > 0)
                .map(productMapper::toDomain)
                .or(() -> {
                    throw new ProductNotFoundException("Produto não existe ou está com estoque zerado.");
                });
    }

    @Override
    public ProductCreateResponse createProduct(ProductRequest request) {
        boolean productExists = repository.findByNameAndCategory(request.getName(), request.getCategory())
                .stream()
                .findAny()
                .isPresent();

        if (productExists) {
            throw new ProductNotFoundException("Produto já cadastrado com esse nome e categoria.");
        }

        var productEntity = productMapper.toEntity(request);
        productEntity = repository.save(productEntity);
        productSearchService.indexProduct(productEntity);
        return new ProductCreateResponse(
                productMapper.toDomain(productEntity),
                "Produto cadastrado com sucesso!"
        );
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        log.info("Deletando {}: ", productId);
        repository.deleteByProductId(productId);
        log.info("Deletando no  Elasticsearch{}: ", productId);
        productSearchService.deleteProduct(productId);
        log.info("Produto deletado com sucesso {}: ", productId);
    }


    @Override
    public ProductResponse updateProductByName(String name, ProductUpdateRequest updates) {
        ProductEntity product = repository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));

        if (updates.getCategory() != null) {
            product.setCategory(updates.getCategory());
        }
        if (updates.getPrice() != null) {
            product.setPrice(updates.getPrice());
        }
        if (updates.getStock() != null) {
            product.setStock(updates.getStock());
        }
        if (updates.getDescription() != null) {
            product.setDescription(updates.getDescription());
        }


        product.setUpdateDate(LocalDateTime.now());
        ProductEntity updatedProduct = repository.save(product);
        productSearchService.indexProduct(updatedProduct);
        return productMapper.toDomain(updatedProduct);
    }


    @Override
    public Map<UUID, ProductStockResponse> checkStock(List<UUID> productIds) {
        List<ProductEntity> products = repository.findByProductIdIn(productIds);

        if (products.isEmpty()) {
            throw new ProductNotFoundException("Nenhum dos produtos foi encontrado.");
        }

        List<UUID> foundProductIds = products.stream()
                .map(ProductEntity::getProductId)
                .toList();

        List<UUID> missingProductIds = productIds.stream()
                .filter(id -> !foundProductIds.contains(id))
                .toList();

        if (!missingProductIds.isEmpty()) {
            log.warn("Os seguintes produtos não foram encontrados: " + missingProductIds);
        }

        return products.stream()
                .collect(Collectors.toMap(
                        ProductEntity::getProductId,
                        product -> new ProductStockResponse(
                                product.getProductId(),
                                product.getPrice(),
                                product.getStock()
                        )
                ));
    }

}

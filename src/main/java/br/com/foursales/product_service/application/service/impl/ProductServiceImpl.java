package br.com.foursales.product_service.application.service.impl;

import br.com.foursales.product_service.application.service.ProductService;
import br.com.foursales.product_service.domain.model.ProductCreateResponse;
import br.com.foursales.product_service.domain.model.ProductRequest;
import br.com.foursales.product_service.domain.model.ProductResponse;
import br.com.foursales.product_service.domain.model.ProductUpdateRequest;
import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import br.com.foursales.product_service.infrastructure.persistence.repository.ProductRepository;
import br.com.foursales.product_service.infrastructure.persistence.repository.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> getAllProducts() {
        return repository.findAll().stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponse> getProductById(Long id) {
        return repository.findById(id).map(productMapper::toDomain);
    }

    @Override
    public ProductCreateResponse createProduct(ProductRequest request) {
        List<ProductEntity> existingProducts = repository.findByNameAndCategory(request.getName(), request.getCategory());

        for (ProductEntity existingProduct : existingProducts) {
            if (existingProduct.getPrice().equals(request.getPrice()) &&
                    existingProduct.getStock().equals(request.getStock())) {
                throw new IllegalArgumentException("Produto já cadastrado com essas informações.");
            }
        }

        ProductEntity productEntity;
        boolean atualizado = false;

        if (!existingProducts.isEmpty()) {
            productEntity = existingProducts.get(0); // Pega o primeiro produto encontrado
            productEntity.setStock(productEntity.getStock() + request.getStock()); // Atualiza o estoque
            productEntity.setPrice(request.getPrice()); // Atualiza o preço
            atualizado = true;
        } else {
            productEntity = productMapper.toEntity(request);
        }

        productEntity = repository.save(productEntity);

        return new ProductCreateResponse(
                productMapper.toDomain(productEntity),
                atualizado ? "Produto já existia. Estoque e preço atualizados!" : "Produto cadastrado com sucesso!"
        );
    }

    @Override
    public void deleteProduct(Long id) {
        repository.deleteById(id);
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

        ProductEntity updatedProduct = repository.save(product);
        return productMapper.toDomain(updatedProduct);
    }


}

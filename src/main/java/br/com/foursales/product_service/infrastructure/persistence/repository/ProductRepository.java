package br.com.foursales.product_service.infrastructure.persistence.repository;


import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByNameAndCategory(String name, String category);

    Optional<ProductEntity> findByName(String name);

    Optional<ProductEntity> findByProductId(UUID productId);

    void deleteByProductId(UUID productId);

    List<ProductEntity> findByProductIdIn(List<UUID> productIds);

}

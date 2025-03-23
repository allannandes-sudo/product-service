package br.com.foursales.product_service.infrastructure.persistence.repository;

import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByNameAndCategory(String name, String category); // Busca apenas o primeiro

    Optional<ProductEntity> findByName(String name);

}

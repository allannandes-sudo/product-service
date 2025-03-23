package br.com.foursales.product_service.infrastructure.persistence.repository.mapper;

import br.com.foursales.product_service.domain.model.ProductRequest;
import br.com.foursales.product_service.domain.model.ProductResponse;
import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toDomain(ProductEntity entity);

    ProductEntity toEntity(ProductRequest productRequest);
}

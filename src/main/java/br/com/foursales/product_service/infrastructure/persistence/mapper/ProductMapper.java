package br.com.foursales.product_service.infrastructure.persistence.mapper;

import br.com.foursales.product_service.domain.model.ProductRequest;
import br.com.foursales.product_service.domain.model.ProductResponse;
import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;
@Mapper(
        componentModel = "spring",
        imports = {UUID.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {


    ProductResponse toDomain(ProductEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "productId", expression = "java(UUID.randomUUID())")
    ProductEntity toEntity(ProductRequest productRequest);
}

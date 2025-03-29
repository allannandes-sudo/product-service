package br.com.foursales.product_service.infrastructure.search.mapper;

import br.com.foursales.product_service.infrastructure.persistence.entity.ProductEntity;
import br.com.foursales.product_service.infrastructure.search.ProductDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDocumentMapper {

    @Mapping(source = "productId", target = "productId", qualifiedByName = "uuidToString")
    @Mapping(target = "createDate", source = "creationDate", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "updateDate", source = "updateDate", qualifiedByName = "localDateTimeToString")
    ProductDocument toDocument(ProductEntity product);

    @Named("uuidToString")
    static String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("localDateTimeToString")
    static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.atZone(ZoneOffset.UTC).toString() : null;
    }
}
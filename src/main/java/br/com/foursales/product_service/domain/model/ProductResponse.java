package br.com.foursales.product_service.domain.model;

import br.com.foursales.product_service.web.annotations.ZonedDataTimeFormatter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    @JsonIgnore
    private Long id;
    private UUID productId;
    private String name;
    private String description;
    private String category;
    private Double price;
    private Integer stock;
    @ZonedDataTimeFormatter
    private LocalDateTime creationDate;
    @ZonedDataTimeFormatter
    private LocalDateTime updateDate;
}

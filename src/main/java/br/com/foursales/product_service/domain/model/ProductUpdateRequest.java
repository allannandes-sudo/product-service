package br.com.foursales.product_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {
    private String category;
    private Double price;
    private Integer stock;

    public ProductUpdateRequest(Integer stock) {
        this.stock = stock;
    }
}


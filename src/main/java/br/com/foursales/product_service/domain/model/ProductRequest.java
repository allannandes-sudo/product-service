package br.com.foursales.product_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
//    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer stock;
}

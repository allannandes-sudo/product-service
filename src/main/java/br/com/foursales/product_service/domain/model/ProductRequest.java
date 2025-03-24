package br.com.foursales.product_service.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "O nome do produto não pode estar vazio")
    private String name;

    @NotBlank(message = "A categoria do produto não pode estar vazia")
    private String category;

    @NotNull(message = "O preço do produto deve ser informado")
    @Min(value = 0, message = "O preço do produto não pode ser negativo")
    private Double price;

    @NotNull(message = "O estoque do produto deve ser informado")
    @Min(value = 0, message = "O estoque do produto não pode ser negativo")
    private Integer stock;
}
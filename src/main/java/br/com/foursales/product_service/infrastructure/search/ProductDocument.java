package br.com.foursales.product_service.infrastructure.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@Document(indexName = "products")
@AllArgsConstructor
public class ProductDocument {

    @Id
    private String id;
    private String name;
    private String category;
    private Double price;
    private Integer stock;

}

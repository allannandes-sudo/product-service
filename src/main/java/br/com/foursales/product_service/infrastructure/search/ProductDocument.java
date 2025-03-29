package br.com.foursales.product_service.infrastructure.search;

import co.elastic.clients.json.JsonpDeserializer;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.JsonpSerializable;
import jakarta.json.stream.JsonGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@Document(indexName = "products")
public class ProductDocument implements JsonpSerializable {

    @Id
    private String id;
    private String productId;
    private String name;
    private String category;
    private Double price;
    private Integer stockQuantity;
    private String createDate;
    private String updateDate;

    // Método obrigatório para serialização JSON
    @Override
    public void serialize(JsonGenerator generator, JsonpMapper mapper) {
        generator.writeStartObject();
        generator.write("id", id);
        generator.write("productId", productId);
        generator.write("name", name);
        generator.write("category", category);
        generator.write("price", price);
        generator.write("stockQuantity", stockQuantity);
        generator.write("createDate", createDate);
        generator.write("updateDate", updateDate);
        generator.writeEnd();
    }

    // Deserialização para reconstruir o objeto ao buscar no Elasticsearch
    public static final JsonpDeserializer<ProductDocument> DESERIALIZER =
            JsonpDeserializer.of(ProductDocument.class);
}

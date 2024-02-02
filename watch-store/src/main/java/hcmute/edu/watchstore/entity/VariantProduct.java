package hcmute.edu.watchstore.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "VariantProduct")
@AllArgsConstructor
@NoArgsConstructor
public class VariantProduct {
    @Id
    private ObjectId id;

    private String img;

    private String color;

    private String edition;

    private double price;

    private ObjectId product;

    private String state;
}
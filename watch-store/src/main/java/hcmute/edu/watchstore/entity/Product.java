package hcmute.edu.watchstore.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "Product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private ObjectId id;

    private String productName;

    private String img;

    private double price;

    private String size;

    private String color;

    private String brand;
    
    private String origin;

    private ObjectId category;

    private String state;
}

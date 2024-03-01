package hcmute.edu.watchstore.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "ProductItem")
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {
    @Id
    private ObjectId id;

    private ObjectId product;

    private int quantity;
}

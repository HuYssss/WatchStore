package hcmute.edu.watchstore.entity;


import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "Order")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private ObjectId id;

    private Date orderDate;

    private double totalPrice;

    private List<ObjectId> productItems;

    private ObjectId user;

    private ObjectId address;

    private String state;
}

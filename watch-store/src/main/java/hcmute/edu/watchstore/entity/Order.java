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

    private List<ObjectId> orderItems;

    private String shippingAddress;

    private String paymentMethod;

    private double itemsPrice;

    private double shippingPrice;

    private double totalPrice;

    private ObjectId user;

    private boolean isPaid;

    private Date paidAt;

    private boolean isDelivered;

    private Date deliveredAt;

    private Date createdAt;

    private String state;
}

package hcmute.edu.watchstore.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Coupon")
public class Coupon {
    @Id
    private ObjectId id;

    private String name;

    private int quantity;

    private double sale;

    private List<ObjectId> condition;

    private String state;
}

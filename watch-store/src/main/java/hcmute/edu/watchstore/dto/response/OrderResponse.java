package hcmute.edu.watchstore.dto.response;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import hcmute.edu.watchstore.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private ObjectId id;

    private Date orderDate;

    private double totalPrice;

    private ObjectId coupon;

    private List<ProductItemResponse> productItems;

    private ObjectId user;

    private Address address;

    private String state;
}

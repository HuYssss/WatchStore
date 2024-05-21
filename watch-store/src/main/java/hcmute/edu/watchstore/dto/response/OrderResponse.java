package hcmute.edu.watchstore.dto.response;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String id;

    private Date orderDate;

    private double totalPrice;

    private List<ProductItemResponse> productItems;

    private ObjectId user;

    private String address;

    private String state;
}

package hcmute.edu.watchstore.dto.request;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyNowRequest {
    private ObjectId product;
    private int quantity;
    private String address;
    private String paymentMethod;
}

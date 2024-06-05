package hcmute.edu.watchstore.dto.request;

import java.util.List;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private List<ObjectId> productItem;
    private String address;
    private String paymentMethod;
}

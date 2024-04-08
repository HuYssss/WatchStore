package hcmute.edu.watchstore.dto.response;

import org.bson.types.ObjectId;

import hcmute.edu.watchstore.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemResponse {
    private ObjectId id;
    private Product product;
    private int quantity;
}

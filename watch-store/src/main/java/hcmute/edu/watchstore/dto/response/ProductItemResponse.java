package hcmute.edu.watchstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemResponse {
    private String id;
    private ProductResponse product;
    private int quantity;
}

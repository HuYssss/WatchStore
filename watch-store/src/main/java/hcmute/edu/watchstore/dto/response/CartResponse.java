package hcmute.edu.watchstore.dto.response;

import java.util.List;

import hcmute.edu.watchstore.entity.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private List<ProductItem> productItems;
}

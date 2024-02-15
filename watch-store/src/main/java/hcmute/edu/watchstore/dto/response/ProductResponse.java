package hcmute.edu.watchstore.dto.response;

import java.util.List;

import org.bson.types.ObjectId;

import hcmute.edu.watchstore.entity.Product;
import hcmute.edu.watchstore.entity.VariantProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private ObjectId id;

    private String productName;

    private String size;

    private String origin;

    private List<VariantProduct> variantProducts;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.size = product.getSize();
        this.origin = product.getOrigin();
    }
}

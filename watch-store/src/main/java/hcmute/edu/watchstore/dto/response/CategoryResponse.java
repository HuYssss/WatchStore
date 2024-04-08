package hcmute.edu.watchstore.dto.response;

import java.util.List;

import org.bson.types.ObjectId;

import hcmute.edu.watchstore.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    
    private ObjectId id;

    private String categoryName;

    private List<Product> product;
    
}

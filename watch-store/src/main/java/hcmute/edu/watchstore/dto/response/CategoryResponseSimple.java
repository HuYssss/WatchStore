package hcmute.edu.watchstore.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseSimple {
    private String id;

    private String categoryName;

    private List<String> productId;
}

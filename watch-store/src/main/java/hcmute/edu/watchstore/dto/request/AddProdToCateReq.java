package hcmute.edu.watchstore.dto.request;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProdToCateReq {
    private ObjectId productId;
    private ObjectId categoryId;
}

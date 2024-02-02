package hcmute.edu.watchstore.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "Category")
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    private ObjectId id;

    private String categoryName;

    private List<ObjectId> product;
}

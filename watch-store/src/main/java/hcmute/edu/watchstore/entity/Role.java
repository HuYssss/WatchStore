package hcmute.edu.watchstore.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "Role")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    private ObjectId id;

    private String roleName;
}

package hcmute.edu.watchstore.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Comment")
public class Comment {
    @Id
    private ObjectId id;
    
    private String content;

    private ObjectId product;

    private ObjectId user;
}

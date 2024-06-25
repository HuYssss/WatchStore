package hcmute.edu.watchstore.entity;

import java.util.*;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Comment")
public class Comment {
    @Id
    private ObjectId id;

    private int star;
    
    private String content;

    private ObjectId product;

    private Date createdOn;

    private ObjectId user;
}

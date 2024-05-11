package hcmute.edu.watchstore.entity;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "AuthToken")
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken {

    @Id
    private ObjectId id;

    private String token;

    private Date expiration;
    
}

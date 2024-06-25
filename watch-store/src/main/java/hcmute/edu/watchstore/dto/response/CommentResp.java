package hcmute.edu.watchstore.dto.response;

import java.util.*;

import hcmute.edu.watchstore.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResp {
    private String id;

    private int star;
    
    private String content;

    private String product;

    private Date createdOn;

    private UserResp user;

    public CommentResp(Comment comment) {
        this.id = comment.getId().toHexString();
        this.star = comment.getStar();
        this.content = comment.getContent();
        this.product = comment.getProduct().toHexString();
        this.createdOn = comment.getCreatedOn();
    }
}

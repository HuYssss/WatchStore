package hcmute.edu.watchstore.service;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import hcmute.edu.watchstore.entity.Comment;

public interface CommentService {
    ObjectId saveOrEditComment(Comment comment);
    boolean deleteComment(ObjectId commentId);
    ResponseEntity<?> createNewComment(Comment comment);
    ResponseEntity<?> editComment(Comment comment, ObjectId userId);
    ResponseEntity<?> deleteCommentById(ObjectId commentId, ObjectId userId);
    ResponseEntity<?> findCommentByProductId(ObjectId productId);
    ResponseEntity<?> getAll();
}

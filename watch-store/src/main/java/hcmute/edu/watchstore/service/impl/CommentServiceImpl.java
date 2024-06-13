package hcmute.edu.watchstore.service.impl;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.constants.ResponseCode;
import hcmute.edu.watchstore.entity.Comment;
import hcmute.edu.watchstore.repository.CommentRepository;
import hcmute.edu.watchstore.service.CommentService;

@Service
public class CommentServiceImpl extends ServiceBase implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public ObjectId saveOrEditComment(Comment comment) {
        if (comment.getId() == null) {
            comment.setId(new ObjectId());
        }

        try {
            this.commentRepository.save(comment);
            return comment.getId();
        } catch (MongoException e) {
            return null;
        }
    }

    @Override
    public boolean deleteComment(ObjectId commentId) {
        try {
            this.commentRepository.deleteById(commentId);
            return true;
        } catch (MongoException e) {
            return false;
        }
    }

    @Override
    public ResponseEntity<?> createNewComment(Comment comment) {
        if (saveOrEditComment(comment) != null) {
            return success("Create comment success");
        } else {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> editComment(Comment comment, ObjectId userId) {
        Optional<Comment> currentComment = this.commentRepository.findById(comment.getId());
        if (!currentComment.isPresent() || !currentComment.get().getUser().equals(userId)) {
            error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }

        comment.setUser(userId);

        if (saveOrEditComment(comment) != null) {
            return success("Edit comment success !!!");
        }
        else {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteCommentById(ObjectId commentId, ObjectId userId) {
        Optional<Comment> currentComment = this.commentRepository.findById(commentId);

        if (!currentComment.isPresent() || !currentComment.get().getUser().equals(userId)) {
            error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }

        if (deleteComment(commentId)) {
            return success("Delete comment success !!!");
        } else {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }
    
}

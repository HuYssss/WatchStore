package hcmute.edu.watchstore.controller;

import org.springframework.web.bind.annotation.RestController;

import hcmute.edu.watchstore.base.ControllerBase;
import hcmute.edu.watchstore.entity.Comment;
import hcmute.edu.watchstore.service.CommentService;

import java.security.Principal;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/comment")
public class CommentController extends ControllerBase {
    @Autowired
    private CommentService commentService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return this.commentService.getAll();
    }

    @PostMapping("/createComment")
    public ResponseEntity<?> createComment(@RequestBody Comment comment, Principal principal) {
        comment.setUser(findIdByUsername(principal.getName()));
        return this.commentService.createNewComment(comment);
    }

    @PostMapping("/editComment")
    public ResponseEntity<?> editComment(@RequestBody Comment comment, Principal principal) {
        return this.commentService.editComment(comment, findIdByUsername(principal.getName()));
    }
    
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable ObjectId commentId, Principal principal) {
        return this.commentService.deleteCommentById(commentId, findIdByUsername(principal.getName()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getCommentByProductId(@PathVariable ObjectId productId) {
        return this.commentService.findCommentByProductId(productId);
    }
}

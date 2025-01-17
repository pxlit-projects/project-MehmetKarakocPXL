package be.pxl.services.controller;

import be.pxl.services.controller.request.CommentRequest;
import be.pxl.services.services.ICommentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class CommentController {
    private final ICommentService commentService;
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @GetMapping("/{postId}")
    public ResponseEntity getAllCommentsByPostId(@PathVariable Long postId, @RequestHeader("Role") String role){
        if (!role.equalsIgnoreCase("user")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Unauthorized role
        }
        return new ResponseEntity<>(commentService.getAllCommentsByPostId(postId), HttpStatus.OK);
    }
    @PostMapping("/{postId}")
    public ResponseEntity<Void> addCommentToPost(@PathVariable Long postId, @RequestBody CommentRequest commentRequest, @RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase("user")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Unauthorized role
        }
        log.info("Comment added to post");
        commentService.addCommentToPost(postId, commentRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long commentId,@RequestBody CommentRequest commentRequest, @RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase("user")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Unauthorized role
        }
        commentService.updateComment(commentId, commentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase("user")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Unauthorized role
        }
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }


}

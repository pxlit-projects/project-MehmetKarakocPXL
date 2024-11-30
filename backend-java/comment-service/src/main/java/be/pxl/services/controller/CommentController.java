package be.pxl.services.controller;

import be.pxl.services.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity getAllCommentsByPostId(@PathVariable Long postId){
        return new ResponseEntity(commentService.getAllCommentsByPostId(postId), HttpStatus.OK);
    }

}

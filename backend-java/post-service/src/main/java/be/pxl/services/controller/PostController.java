package be.pxl.services.controller;

import be.pxl.services.controller.Request.PostRequest;
import be.pxl.services.controller.Response.PostResponse;
import be.pxl.services.domain.Post;
import be.pxl.services.services.IPostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class PostController {

    private IPostService postService;

    @GetMapping
    public ResponseEntity getAllPosts(){
        return new ResponseEntity(postService.getAllPosts(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> addEmployee(@RequestBody PostRequest postRequest) {
        return new ResponseEntity<>(postService.addPost(postRequest), HttpStatus.CREATED);
    }

    //update alles
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest){
        return new ResponseEntity<>(postService.updatePost(postId, postRequest), HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Post>> getPostsSorted(@RequestParam String sortBy) {
        List<Post> sortedPosts = postService.getAllPostsSorted(sortBy);
        return ResponseEntity.ok(sortedPosts);
    }
}

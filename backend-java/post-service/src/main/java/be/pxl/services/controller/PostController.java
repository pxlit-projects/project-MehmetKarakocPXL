package be.pxl.services.controller;

import be.pxl.services.controller.request.PostRequest;
import be.pxl.services.controller.request.NotificationRequest;
import be.pxl.services.controller.response.PostResponse;
import be.pxl.services.domain.Notification;
import be.pxl.services.services.IPostService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    private IPostService postService;
    private static final Logger log = LoggerFactory.getLogger(PostController.class);


    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/published")
    public ResponseEntity<List<PostResponse>> getAllPublishedPosts(){
        return ResponseEntity.ok(postService.getAllPublishedPosts());
    }

    @GetMapping("/to-be-reviewed")
    public ResponseEntity<List<PostResponse>> getAllToBeReviewedPosts(){
        return ResponseEntity.ok(postService.getAllToBeReviewedPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId){
        return new ResponseEntity<>(postService.getPostById(postId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addPost(@RequestBody PostRequest postRequest) {
        postService.addPost(postRequest);
        log.info("Post added");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //update alles
    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest){
        postService.updatePost(postId, postRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PostResponse>> getFilteredPosts(@RequestParam(required = false) String content,
                                                               @RequestParam(required = false) String author,
                                                               @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(postService.getFilteredPosts(content, author, date));
    }

    @GetMapping("/author/{postId}")
    public ResponseEntity<String> getAuthorByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getAuthorByPostId(postId));
    }

    @PostMapping("/notification")
    public ResponseEntity<Void> receiveNotification(@RequestBody NotificationRequest notificationRequest) {
        postService.saveNotification(notificationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notification/{author}")
    public ResponseEntity<List<Notification>> getNotificationsByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(postService.getNotificationsByAuthor(author));
    }



}

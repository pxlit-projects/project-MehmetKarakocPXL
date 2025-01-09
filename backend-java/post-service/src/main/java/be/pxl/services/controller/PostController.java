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
public class PostController {

    private IPostService postService;
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId, @RequestHeader("Role") String role){
        // Example role-based logic
        if (!role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("user")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Unauthorized role
        }
        return new ResponseEntity<>(postService.getPostById(postId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addPost(@RequestBody PostRequest postRequest, @RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        postService.addPost(postRequest);
        log.info("Post added by role: {}", role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody PostRequest postRequest, @RequestHeader("Role") String role){
        // Example role-based logic
        if (!role.equalsIgnoreCase("admin") ) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Unauthorized role
        }
        postService.updatePost(postId, postRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<PostResponse>> getFilteredPosts(@RequestParam(required = false) String content,
                                                               @RequestParam(required = false) String author,
                                                               @RequestParam(required = false) LocalDate date,
                                                               @RequestParam(required = false) String status,
                                                               @RequestHeader("Role") String role
    ) {
        if (!role.equalsIgnoreCase("admin")  && !role.equalsIgnoreCase("user")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Unauthorized role
        }
        return ResponseEntity.ok(postService.getFilteredPosts(content, author, date, status));
    }

    @GetMapping("/author/{postId}")
    public ResponseEntity<String> getAuthorByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getAuthorByPostId(postId));
    }

    @PostMapping("/notification")
    public ResponseEntity<Void> receiveNotification(@RequestBody NotificationRequest notificationRequest) {
        postService.saveNotification(notificationRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/notification/{receiver}")
    public ResponseEntity<List<Notification>> getNotificationsByReceiver(@PathVariable String receiver, @RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase("admin")  && !role.equalsIgnoreCase("user")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Unauthorized role
        }
        return ResponseEntity.ok(postService.getNotificationsByReceiver(receiver));
    }
}

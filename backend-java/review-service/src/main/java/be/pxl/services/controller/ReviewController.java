package be.pxl.services.controller;

import be.pxl.services.client.NotificationClient;
import be.pxl.services.controller.request.ReviewRequest;
import be.pxl.services.controller.response.ReviewResponse;
import be.pxl.services.services.ReviewService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@AllArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {
    private final ReviewService reviewService;
    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    @PostMapping("")
    public ResponseEntity<Void> addReview(@RequestBody ReviewRequest reviewRequest) {
        reviewService.addReview(reviewRequest);
        log.info("Review added for post ID: {}", reviewRequest.postId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ReviewResponse> getReviewByPostId(@PathVariable Long postId){
        return new ResponseEntity<>(reviewService.getReviewByPostId(postId), HttpStatus.OK);
    }

}

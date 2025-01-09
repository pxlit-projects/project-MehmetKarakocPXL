package be.pxl.services.controller;

import be.pxl.services.controller.request.ReviewRequest;
import be.pxl.services.controller.response.ReviewResponse;
import be.pxl.services.services.IReviewService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class ReviewController {
    private final IReviewService reviewService;
    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    @PostMapping
    public ResponseEntity<Void> addReview(@RequestBody ReviewRequest reviewRequest, @RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Unauthorized role
        }
        reviewService.addReview(reviewRequest);
        log.info("Review added for post ID: {}", reviewRequest.postId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviews(@RequestHeader("Role") String role){
        if (!role.equalsIgnoreCase("admin")  && !role.equalsIgnoreCase("user")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Unauthorized role
        }
        return new ResponseEntity<>(reviewService.getReviews(), HttpStatus.OK);
    }

}

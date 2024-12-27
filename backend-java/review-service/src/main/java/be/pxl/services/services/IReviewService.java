package be.pxl.services.services;

import be.pxl.services.controller.request.ReviewRequest;
import be.pxl.services.controller.response.ReviewResponse;

import java.util.List;

public interface IReviewService {

    void addReview(ReviewRequest reviewRequest);

    ReviewResponse getReviewByPostId(Long postId);

    List<ReviewResponse> getReviews();
}

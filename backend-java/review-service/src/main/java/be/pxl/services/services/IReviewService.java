package be.pxl.services.services;

import be.pxl.services.controller.request.ReviewRequest;

public interface IReviewService {

    void addReview(ReviewRequest reviewRequest);
}

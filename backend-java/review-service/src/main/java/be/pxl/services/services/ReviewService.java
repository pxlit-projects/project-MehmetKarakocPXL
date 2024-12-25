package be.pxl.services.services;

import be.pxl.services.client.NotificationClient;
import be.pxl.services.controller.request.ReviewRequest;
import be.pxl.services.controller.response.ReviewResponse;
import be.pxl.services.controller.request.NotificationRequest;
import be.pxl.services.domain.Review;
import be.pxl.services.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final RabbitTemplate rabbitTemplate;
    private final NotificationClient notificationClient;

    @Override
    public void addReview(ReviewRequest reviewRequest) {
        Review review = reviewRepository.findByPostId(reviewRequest.postId());
        if (review == null) {
            // Create a new review if it doesn't exist
            review = new Review();
            review.setPostId(reviewRequest.postId());
        }
        review.setApproved(reviewRequest.isApproved());
        review.setContent(reviewRequest.content());
        review.setAuthor(reviewRequest.author());
        reviewRepository.save(review);

        String authorPost = notificationClient.getAuthor(reviewRequest.postId());

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setAuthor(review.getAuthor());
        notificationRequest.setReceiver(authorPost);
        notificationRequest.setMessage(
                review.isApproved() ? "Your review has been approved" : "Your review has been rejected"
        );
        notificationClient.sendNotification(notificationRequest);
        rabbitTemplate.convertAndSend("myQueue", review);
    }

    public ReviewResponse getReviewByPostId(Long postId) {
        Review review = reviewRepository.findByPostId(postId);
        if (review == null) {
            return null;
        }
        return new ReviewResponse(review.getId(), review.getContent(), review.getPostId(), review.getAuthor(), review.isApproved());
    }
}

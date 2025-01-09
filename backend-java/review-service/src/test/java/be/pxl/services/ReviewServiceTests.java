package be.pxl.services;

import be.pxl.services.client.NotificationClient;
import be.pxl.services.controller.request.ReviewRequest;
import be.pxl.services.controller.response.ReviewResponse;
import be.pxl.services.controller.request.NotificationRequest;
import be.pxl.services.domain.Review;
import be.pxl.services.repository.ReviewRepository;
import be.pxl.services.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddReview_NewReview() {
        ReviewRequest reviewRequest = ReviewRequest.builder()
                .postId(1L)
                .author("Author")
                .content("Great post!")
                .isApproved(true)
                .build();

        when(reviewRepository.findByPostId(reviewRequest.postId())).thenReturn(null);
        when(notificationClient.getAuthor(reviewRequest.postId())).thenReturn("PostAuthor");

        reviewService.addReview(reviewRequest);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        verify(notificationClient).sendNotification(any(NotificationRequest.class));
        verify(rabbitTemplate).convertAndSend(eq("myQueue"), any(Review.class));

        Review savedReview = reviewCaptor.getValue();
        assertEquals(reviewRequest.postId(), savedReview.getPostId());
        assertEquals(reviewRequest.content(), savedReview.getContent());
        assertEquals(reviewRequest.author(), savedReview.getAuthor());
        assertTrue(savedReview.isApproved());
    }

    @Test
    public void testAddReview_UpdateExistingReview() {
        Review existingReview = new Review();
        existingReview.setPostId(1L);
        existingReview.setAuthor("OldAuthor");
        existingReview.setContent("Old content");

        ReviewRequest reviewRequest = ReviewRequest.builder()
                .postId(1L)
                .author("NewAuthor")
                .content("Updated content")
                .isApproved(false)
                .build();

        when(reviewRepository.findByPostId(reviewRequest.postId())).thenReturn(existingReview);
        when(notificationClient.getAuthor(reviewRequest.postId())).thenReturn("PostAuthor");

        reviewService.addReview(reviewRequest);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());

        Review updatedReview = reviewCaptor.getValue();
        assertEquals(reviewRequest.postId(), updatedReview.getPostId());
        assertEquals(reviewRequest.content(), updatedReview.getContent());
        assertEquals(reviewRequest.author(), updatedReview.getAuthor());
        assertFalse(updatedReview.isApproved());
    }

    @Test
    public void testGetReviewByPostId_Found() {
        Review review = new Review();
        review.setId(1L);
        review.setPostId(1L);
        review.setAuthor("Author");
        review.setContent("Content");
        review.setApproved(true);

        when(reviewRepository.findByPostId(1L)).thenReturn(review);

        ReviewResponse response = reviewService.getReviewByPostId(1L);

        assertNotNull(response);
        assertEquals(review.getId(), response.id());
        assertEquals(review.getPostId(), response.postId());
        assertEquals(review.getContent(), response.content());
        assertEquals(review.getAuthor(), response.author());
        assertTrue(response.isApproved());
    }

    @Test
    public void testGetReviewByPostId_NotFound() {
        when(reviewRepository.findByPostId(1L)).thenReturn(null);

        ReviewResponse response = reviewService.getReviewByPostId(1L);

        assertNull(response);
    }

    @Test
    public void testGetReviews() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(1L, 2L, "Author1", "Content1", true));
        reviews.add(new Review(2L, 1L, "Author2", "Content2", false));

        when(reviewRepository.findAll()).thenReturn(reviews);

        List<ReviewResponse> responses = reviewService.getReviews();

        assertEquals(2, responses.size());
        assertEquals("Content1", responses.get(0).content());
        assertEquals("Author1", responses.get(0).author());
        assertTrue(responses.get(0).isApproved());
        assertEquals("Content2", responses.get(1).content());
        assertEquals("Author2", responses.get(1).author());
        assertFalse(responses.get(1).isApproved());
    }
}

package be.pxl.services;

import be.pxl.services.controller.request.NotificationRequest;
import be.pxl.services.domain.Notification;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.Review;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PostDomainTests {
    @Test
    void testPostDefaultConstructor() {
        Post post = new Post();

        assertNull(post.getId());
        assertNull(post.getAuthor());
        assertNull(post.getTitle());
        assertNull(post.getContent());
        assertNull(post.getIsConcept());
        assertNull(post.getStatus());
        assertNull(post.getCreatedDate());
    }


    @Test
    void testReviewDefaultConstructor() {
        // Arrange
        Review review = new Review();

        // Assert
        assertNull(review.getId(), "ID should be null with default constructor");
        assertNull(review.getPostId(), "Post ID should be null with default constructor");
        assertNull(review.getAuthor(), "Author should be null with default constructor");
        assertNull(review.getContent(), "Content should be null with default constructor");
        assertFalse(review.isApproved(), "isApproved should be false with default constructor");
    }


    @Test
    void testNotificationDefaultConstructor() {
        // Arrange
        Notification notification = new Notification();

        // Assert
        assertNull(notification.getId(), "ID should be null with default constructor");
        assertNull(notification.getMessage(), "Message should be null with default constructor");
        assertNull(notification.getAuthor(), "Author should be null with default constructor");
        assertNull(notification.getReceiver(), "Receiver should be null with default constructor");
        assertNull(notification.getCreatedDate(), "Created date should be null with default constructor");
    }
}

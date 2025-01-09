package be.pxl.services;

import be.pxl.services.controller.request.NotificationRequest;
import be.pxl.services.domain.Review;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewTests {

    @Test
    public void testReviewBuilderAndGetters() {
        // Arrange
        Long id = 1L;
        Long postId = 101L;
        String author = "Author Name";
        String content = "This is a great post!";
        boolean isApproved = true;

        // Act
        Review review = Review.builder()
                .id(id)
                .postId(postId)
                .author(author)
                .content(content)
                .isApproved(isApproved)
                .build();

        // Assert
        assertEquals(id, review.getId());
        assertEquals(postId, review.getPostId());
        assertEquals(author, review.getAuthor());
        assertEquals(content, review.getContent());
        assertTrue(review.isApproved());
    }

    @Test
    public void testReviewNoArgsConstructor() {
        // Act
        Review review = new Review();

        // Assert
        assertNull(review.getId());
        assertNull(review.getPostId());
        assertNull(review.getAuthor());
        assertNull(review.getContent());
        assertFalse(review.isApproved());
    }

    @Test
    public void testReviewAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        Long postId = 101L;
        String author = "Author Name";
        String content = "This is a great post!";
        boolean isApproved = true;

        // Act
        Review review = new Review(id, postId, author, content, isApproved);

        // Assert
        assertEquals(id, review.getId());
        assertEquals(postId, review.getPostId());
        assertEquals(author, review.getAuthor());
        assertEquals(content, review.getContent());
        assertTrue(review.isApproved());
    }

    @Test
    public void testReviewSetters() {
        // Arrange
        Review review = new Review();
        Long id = 1L;
        Long postId = 101L;
        String author = "Author Name";
        String content = "This is a great post!";
        boolean isApproved = true;

        // Act
        review.setId(id);
        review.setPostId(postId);
        review.setAuthor(author);
        review.setContent(content);
        review.setApproved(isApproved);

        // Assert
        assertEquals(id, review.getId());
        assertEquals(postId, review.getPostId());
        assertEquals(author, review.getAuthor());
        assertEquals(content, review.getContent());
        assertTrue(review.isApproved());
    }

    @Test
    public void testReviewEquality() {
        // Arrange
        Review review1 = new Review(1L, 101L, "Author", "Content", true);
        Review review2 = new Review(1L, 101L, "Author", "Content", true);

        // Act & Assert
        assertEquals(review1, review2);
        assertEquals(review1.hashCode(), review2.hashCode());
    }

    @Test
    public void testReviewToString() {
        // Arrange
        Review review = Review.builder()
                .id(1L)
                .postId(101L)
                .author("Author Name")
                .content("This is a great post!")
                .isApproved(true)
                .build();

        // Act
        String reviewString = review.toString();

        // Assert
        assertTrue(reviewString.contains("id=1"));
        assertTrue(reviewString.contains("postId=101"));
        assertTrue(reviewString.contains("author=Author Name"));
        assertTrue(reviewString.contains("content=This is a great post!"));
        assertTrue(reviewString.contains("isApproved=true"));
    }

    @Test
    void testNotificationRequestDefaultConstructor() {
        NotificationRequest notificationRequest = new NotificationRequest();
        assertNull(notificationRequest.getMessage());
        assertNull(notificationRequest.getAuthor());
        assertNull(notificationRequest.getReceiver());
    }

    @Test
    void testNotificationRequestAllArgsConstructor() {
        NotificationRequest notificationRequest = new NotificationRequest(
                "Test Message",
                "Author Name",
                "Receiver Name"
        );

        assertEquals("Test Message", notificationRequest.getMessage());
        assertEquals("Author Name", notificationRequest.getAuthor());
        assertEquals("Receiver Name", notificationRequest.getReceiver());
    }

    @Test
    void testNotificationRequestBuilder() {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .message("Test Message")
                .author("Author Name")
                .receiver("Receiver Name")
                .build();

        assertEquals("Test Message", notificationRequest.getMessage());
        assertEquals("Author Name", notificationRequest.getAuthor());
        assertEquals("Receiver Name", notificationRequest.getReceiver());
    }

    @Test
    void testSettersAndGetters() {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setMessage("Updated Message");
        notificationRequest.setAuthor("Updated Author");
        notificationRequest.setReceiver("Updated Receiver");

        assertEquals("Updated Message", notificationRequest.getMessage());
        assertEquals("Updated Author", notificationRequest.getAuthor());
        assertEquals("Updated Receiver", notificationRequest.getReceiver());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationRequest notificationRequest1 = NotificationRequest.builder()
                .message("Test Message")
                .author("Author Name")
                .receiver("Receiver Name")
                .build();

        NotificationRequest notificationRequest2 = NotificationRequest.builder()
                .message("Test Message")
                .author("Author Name")
                .receiver("Receiver Name")
                .build();

        assertEquals(notificationRequest1, notificationRequest2);
        assertEquals(notificationRequest1.hashCode(), notificationRequest2.hashCode());
    }

    @Test
    void testToString() {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .message("Test Message")
                .author("Author Name")
                .receiver("Receiver Name")
                .build();

        String expected = "NotificationRequest(message=Test Message, author=Author Name, receiver=Receiver Name)";
        assertEquals(expected, notificationRequest.toString());
    }
}

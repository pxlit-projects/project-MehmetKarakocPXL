
package be.pxl.services;

import be.pxl.services.controller.request.CommentRequest;
import be.pxl.services.domain.Comment;
import be.pxl.services.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class CommentDomainTests {

    @Test
    void shouldCreateCommentUsingAllArgsConstructor() {
        LocalDateTime createdDate = LocalDateTime.now();
        Comment comment = new Comment(1L, 10L, "John Doe", "This is a test comment", createdDate);

        assertThat(comment.getId()).isEqualTo(1L);
        assertThat(comment.getPostId()).isEqualTo(10L);
        assertThat(comment.getAuthor()).isEqualTo("John Doe");
        assertThat(comment.getContent()).isEqualTo("This is a test comment");
        assertThat(comment.getCreatedDate()).isEqualTo(createdDate);
    }

    @Test
    void shouldCreateCommentUsingBuilder() {
        LocalDateTime createdDate = LocalDateTime.now();
        Comment comment = Comment.builder()
                .id(2L)
                .postId(20L)
                .author("Jane Doe")
                .content("Another test comment")
                .createdDate(createdDate)
                .build();

        assertThat(comment.getId()).isEqualTo(2L);
        assertThat(comment.getPostId()).isEqualTo(20L);
        assertThat(comment.getAuthor()).isEqualTo("Jane Doe");
        assertThat(comment.getContent()).isEqualTo("Another test comment");
        assertThat(comment.getCreatedDate()).isEqualTo(createdDate);
    }

    @Test
    void shouldUpdateCommentDetails() {
        Comment comment = new Comment();
        comment.setId(3L);
        comment.setPostId(30L);
        comment.setAuthor("Mark Twain");
        comment.setContent("Editable comment");

        assertThat(comment.getId()).isEqualTo(3L);
        assertThat(comment.getPostId()).isEqualTo(30L);
        assertThat(comment.getAuthor()).isEqualTo("Mark Twain");
        assertThat(comment.getContent()).isEqualTo("Editable comment");

        // Update values
        comment.setContent("Updated comment content");
        assertThat(comment.getContent()).isEqualTo("Updated comment content");
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        Comment comment1 = new Comment(1L, 10L, "Alice", "Sample content", LocalDateTime.now());
        Comment comment2 = new Comment(1L, 10L, "Alice", "Sample content", comment1.getCreatedDate());

        assertThat(comment1).isEqualTo(comment2);
        assertThat(comment1.hashCode()).isEqualTo(comment2.hashCode());
    }

    @Test
    void shouldTestToString() {
        Comment comment = Comment.builder()
                .id(4L)
                .postId(40L)
                .author("Tester")
                .content("Content for toString")
                .build();

        String expectedString = "Comment(id=4, postId=40, author=Tester, content=Content for toString, createdDate=null)";
        assertThat(comment.toString()).isEqualTo(expectedString);
    }
}

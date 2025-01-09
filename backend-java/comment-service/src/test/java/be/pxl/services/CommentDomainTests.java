package be.pxl.services;

import be.pxl.services.controller.request.CommentRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommentDomainTests {

    @Test
    public void testCommentRequestBuilderAndGetters() {
        Long postId = 1L;
        String author = "Author Name";
        String content = "This is a comment.";

        CommentRequest commentRequest = CommentRequest.builder()
                .postId(postId)
                .author(author)
                .content(content)
                .build();

        assertEquals(postId, commentRequest.postId());
        assertEquals(author, commentRequest.author());
        assertEquals(content, commentRequest.content());
    }

    @Test
    public void testCommentRequestEquality() {
        Long postId = 1L;
        String author = "Author Name";
        String content = "This is a comment.";

        CommentRequest commentRequest1 = CommentRequest.builder()
                .postId(postId)
                .author(author)
                .content(content)
                .build();

        CommentRequest commentRequest2 = CommentRequest.builder()
                .postId(postId)
                .author(author)
                .content(content)
                .build();

        assertEquals(commentRequest1, commentRequest2);
    }

    @Test
    public void testCommentRequestToString() {
        Long postId = 1L;
        String author = "Author Name";
        String content = "This is a comment.";

        CommentRequest commentRequest = CommentRequest.builder()
                .postId(postId)
                .author(author)
                .content(content)
                .build();

        String expected = "CommentRequest[postId=1, author=Author Name, content=This is a comment.]";
        assertEquals(expected, commentRequest.toString());
    }

    @Test
    public void testCommentRequestWithNullValues() {
        CommentRequest commentRequest = CommentRequest.builder()
                .postId(null)
                .author(null)
                .content(null)
                .build();

        assertNull(commentRequest.postId());
        assertNull(commentRequest.author());
        assertNull(commentRequest.content());
    }
}

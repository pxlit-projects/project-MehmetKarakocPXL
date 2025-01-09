package be.pxl.services;

import be.pxl.services.controller.request.CommentRequest;
import be.pxl.services.controller.response.CommentResponse;
import be.pxl.services.domain.Comment;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.repository.CommentRepository;
import be.pxl.services.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class CommentServiceTests {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCommentsByPostId() {
        // Arrange
        Long postId = 1L;
        Comment comment = new Comment(1L, postId, "Author", "Content", LocalDateTime.now());
        when(commentRepository.findCommentsByPostId(postId)).thenReturn(List.of(comment));

        // Act
        List<CommentResponse> comments = commentService.getAllCommentsByPostId(postId);

        // Assert
        assertNotNull(comments, "Comment responses should not be null");
        assertEquals(1, comments.size(), "Should return one comment response");
        CommentResponse response = comments.get(0);
        assertEquals(comment.getId(), response.id(), "Comment ID should match");
        assertEquals(comment.getPostId(), response.postId(), "Post ID should match");
        assertEquals(comment.getAuthor(), response.author(), "Author should match");
        assertEquals(comment.getContent(), response.content(), "Content should match");
        assertEquals(comment.getCreatedDate(), response.createdDate(), "Created date should match");
    }

    @Test
    void testAddCommentToPost() {
        // Arrange
        Long postId = 1L;
        CommentRequest request = new CommentRequest(postId, "Author", "Content");
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setAuthor(request.author());
        comment.setContent(request.content());
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        commentService.addCommentToPost(postId, request);

        // Assert
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testUpdateComment_Success() {
        // Arrange
        Long commentId = 1L;
        Comment existingComment = new Comment(commentId, 1L, "Old Author", "Old Content", LocalDateTime.now());
        CommentRequest request = new CommentRequest(1L, "New Author", "New Content");
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(existingComment);

        // Act
        commentService.updateComment(commentId, request);

        // Assert
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).save(existingComment);
        assertEquals("New Author", existingComment.getAuthor(), "Author should be updated");
        assertEquals("New Content", existingComment.getContent(), "Content should be updated");
    }

    @Test
    void testUpdateComment_NotFound() {
        // Arrange
        Long commentId = 1L;
        CommentRequest request = new CommentRequest(1L, "Author", "Content");
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> commentService.updateComment(commentId, request));
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void testDeleteComment_Success() {
        // Arrange
        Long commentId = 1L;
        Comment existingComment = new Comment(commentId, 1L, "Author", "Content", LocalDateTime.now());
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // Act
        commentService.deleteComment(commentId);

        // Assert
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(existingComment);
    }

    @Test
    void testDeleteComment_NotFound() {
        // Arrange
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> commentService.deleteComment(commentId));
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(0)).delete(any(Comment.class));
    }
}

package be.pxl.services;

import be.pxl.services.controller.request.NotificationRequest;
import be.pxl.services.controller.request.PostRequest;
import be.pxl.services.controller.response.PostResponse;
import be.pxl.services.domain.Notification;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.Review;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.repository.NotificationRepository;
import be.pxl.services.repository.PostRepository;
import be.pxl.services.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTests {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPostById_ValidId_ShouldReturnPostResponse() {
        // Arrange
        Post post = Post.builder()
                .id(1L)
                .author("Author")
                .title("Title")
                .content("Content")
                .isConcept(false)
                .status(PostStatus.APPROVED)
                .createdDate(LocalDateTime.now())
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act
        PostResponse response = postService.getPostById(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Author", response.author());
        assertEquals("Title", response.title());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPostById_InvalidId_ShouldThrowNotFoundException() {
        // Arrange
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> postService.getPostById(1L));
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddPost_ShouldSavePost() {
        // Arrange
        PostRequest postRequest = PostRequest.builder()
                .author("Author")
                .title("Title")
                .content("Content")
                .isConcept(false)
                .status(PostStatus.PENDING)
                .build();

        Post post = Post.builder()
                .author(postRequest.author())
                .title(postRequest.title())
                .content(postRequest.content())
                .isConcept(postRequest.isConcept())
                .status(postRequest.status())
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Act
        postService.addPost(postRequest);

        // Assert
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void testUpdatePost_ValidId_ShouldUpdatePost() {
        // Arrange
        Post existingPost = Post.builder()
                .id(1L)
                .author("Old Author")
                .title("Old Title")
                .content("Old Content")
                .isConcept(true)
                .status(PostStatus.PENDING)
                .build();

        PostRequest postRequest = PostRequest.builder()
                .author("New Author")
                .title("New Title")
                .content("New Content")
                .isConcept(false)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));

        // Act
        postService.updatePost(1L, postRequest);

        // Assert
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(existingPost);
        assertEquals("New Author", existingPost.getAuthor());
        assertEquals("New Title", existingPost.getTitle());
        assertEquals("New Content", existingPost.getContent());
        assertFalse(existingPost.getIsConcept());
        assertEquals(PostStatus.PENDING, existingPost.getStatus()); // Status explicitly set to PENDING
    }

    @Test
    public void testUpdatePost_InvalidId_ShouldThrowNotFoundException() {
        // Arrange
        PostRequest postRequest = PostRequest.builder()
                .author("Author")
                .title("Title")
                .content("Content")
                .isConcept(false)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> postService.updatePost(1L, postRequest));
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetFilteredPosts_ShouldReturnFilteredPosts() {
        // Arrange
        Post post1 = Post.builder()
                .id(1L)
                .author("Author 1")
                .title("Title 1")
                .content("Content 1")
                .isConcept(false)
                .status(PostStatus.APPROVED)
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .author("Author 2")
                .title("Title 2")
                .content("Content 2")
                .isConcept(false)
                .status(PostStatus.PENDING)
                .build();

        when(postRepository.findByFilters("Content", "Author", LocalDate.now(), PostStatus.APPROVED))
                .thenReturn(Arrays.asList(post1, post2));

        // Act
        List<PostResponse> responses = postService.getFilteredPosts("Content", "Author", LocalDate.now(), "APPROVED");

        // Assert
        assertEquals(2, responses.size());
        verify(postRepository, times(1)).findByFilters("Content", "Author", LocalDate.now(), PostStatus.APPROVED);
    }

    @Test
    public void testSaveNotification_ShouldSaveNotification() {
        // Arrange
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .author("Author")
                .message("Test Notification")
                .receiver("Receiver")
                .build();

        Notification notification = Notification.builder()
                .author(notificationRequest.getAuthor())
                .message(notificationRequest.getMessage())
                .receiver(notificationRequest.getReceiver())
                .build();

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        postService.saveNotification(notificationRequest);

        // Assert
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    public void testGetNotificationsByReceiver_ShouldReturnNotifications() {
        // Arrange
        Notification notification = Notification.builder()
                .author("Author")
                .message("Test Notification")
                .receiver("Receiver")
                .build();

        when(notificationRepository.findByReceiverOrderByCreatedDateDesc("Receiver"))
                .thenReturn(Arrays.asList(notification));

        // Act
        List<Notification> notifications = postService.getNotificationsByReceiver("Receiver");

        // Assert
        assertEquals(1, notifications.size());
        assertEquals("Test Notification", notifications.get(0).getMessage());
        verify(notificationRepository, times(1)).findByReceiverOrderByCreatedDateDesc("Receiver");
    }
    @Test
    public void testListenReview_ShouldApprovePost() {
        Review review = Review.builder()
                .postId(1L)
                .isApproved(true)
                .build();

        Post post = Post.builder()
                .id(1L)
                .title("Title")
                .status(PostStatus.PENDING)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.listenReview(review);

        assertEquals(PostStatus.APPROVED, post.getStatus());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testListenReview_ShouldRejectPost() {
        Review review = Review.builder()
                .postId(2L)
                .isApproved(false)
                .build();

        Post post = Post.builder()
                .id(2L)
                .title("Title")
                .status(PostStatus.PENDING)
                .build();

        when(postRepository.findById(2L)).thenReturn(Optional.of(post));

        postService.listenReview(review);

        assertEquals(PostStatus.REJECTED, post.getStatus());
        assertTrue(post.getIsConcept());
        verify(postRepository, times(1)).save(post);
    }

}

package be.pxl.services;

import be.pxl.services.controller.request.NotificationRequest;
import be.pxl.services.controller.request.PostRequest;
import be.pxl.services.domain.Notification;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.repository.NotificationRepository;
import be.pxl.services.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class PostControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Container
    private static MySQLContainer sqlContainer =
            new MySQLContainer("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    @AfterEach
    public void clearDatabase() {
        postRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    @Test
    public void testCreatePost_withAdminRole() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("Post Title")
                .content("Post Content")
                .author("Author")
                .isConcept(false)
                .build();

        String postString = objectMapper.writeValueAsString(postRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .header("Role", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postString))
                .andExpect(status().isCreated());

        assertEquals(1, postRepository.findAll().size());
    }

    @Test
    public void testCreatePost_withUserRole_shouldReturnForbidden() throws Exception {
        PostRequest postRequest = PostRequest.builder()
                .title("Post Title")
                .content("Post Content")
                .author("Author")
                .isConcept(false)
                .build();

        String postString = objectMapper.writeValueAsString(postRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .header("Role", "user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postString))
                .andExpect(status().isForbidden());

        assertEquals(0, postRepository.findAll().size());
    }

    @Test
    public void testGetPostById_withInvalidRole_shouldReturnForbidden() throws Exception {
        Post post = Post.builder()
                .title("Post Title")
                .content("Post Content")
                .author("Author")
                .status(PostStatus.APPROVED)
                .isConcept(false)
                .build();
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/" + post.getId())
                        .header("Role", "invalid"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetPostById_withValidRole_shouldReturnPost() throws Exception {
        Post post = Post.builder()
                .title("Post Title")
                .content("Post Content")
                .author("Author")
                .status(PostStatus.APPROVED)
                .isConcept(false)
                .build();
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/" + post.getId())
                        .header("Role", "user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Post Title"));
    }

    @Test
    public void testUpdatePost_withUserRole_shouldReturnForbidden() throws Exception {
        Post post = Post.builder()
                .title("Original Title")
                .content("Original Content")
                .author("Author")
                .status(PostStatus.PENDING)
                .isConcept(false)
                .build();
        postRepository.save(post);

        PostRequest updatedPostRequest = PostRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .isConcept(false)
                .build();

        String updatedPostString = objectMapper.writeValueAsString(updatedPostRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/post/" + post.getId())
                        .header("Role", "user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPostString))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetFilteredPosts_withValidRole_shouldReturnPosts() throws Exception {
        Post post1 = Post.builder()
                .title("Post 1")
                .content("Content 1")
                .author("Author 1")
                .status(PostStatus.APPROVED)
                .isConcept(false)
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("Post 2")
                .content("Content 2")
                .author("Author 2")
                .status(PostStatus.PENDING)
                .isConcept(false)
                .build();
        postRepository.save(post2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/filtered")
                        .header("Role", "admin")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Post 1"));
    }

    @Test
    public void testReceiveNotification_withValidRole_shouldSaveNotification() throws Exception {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .message("Notification Message")
                .receiver("user")
                .author("admin")
                .build();

        String notificationString = objectMapper.writeValueAsString(notificationRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post/notification")
                        .header("Role", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notificationString))
                .andExpect(status().isCreated());

        assertEquals(1, notificationRepository.findAll().size());
    }

    @Test
    public void testGetNotificationsByReceiver_withValidRole_shouldReturnNotifications() throws Exception {
        Notification notification = Notification.builder()
                .message("Test Notification")
                .receiver("user")
                .author("admin")
                .build();

        notificationRepository.save(notification);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/notification/user")
                        .header("Role", "user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Test Notification"));
    }

    @Test
    public void testGetNotificationsByReceiver_withInvalidRole_shouldReturnForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/notification/user")
                        .header("Role", "invalid"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testPostRequestBuilder_ShouldBuildCorrectly() {
        PostRequest postRequest = PostRequest.builder()
                .author("Test Author")
                .title("Test Title")
                .content("Test Content")
                .isConcept(false)
                .status(PostStatus.APPROVED)
                .build();

        assertEquals("Test Author", postRequest.author());
        assertEquals("Test Title", postRequest.title());
        assertEquals("Test Content", postRequest.content());
        assertFalse(postRequest.isConcept());
        assertEquals(PostStatus.APPROVED, postRequest.status());
    }

    @Test
    public void testNotificationBuilder_ShouldBuildCorrectly() {
        Notification notification = Notification.builder()
                .author("Author")
                .message("Test Message")
                .receiver("Receiver")
                .build();

        assertEquals("Author", notification.getAuthor());
        assertEquals("Test Message", notification.getMessage());
        assertEquals("Receiver", notification.getReceiver());
    }
}

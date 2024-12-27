package be.pxl.services;

import be.pxl.services.domain.Notification;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.repository.NotificationRepository;
import be.pxl.services.repository.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class PostTests {

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
    public void testCreatePost() throws Exception {
        Post post = Post.builder()
                .title("Post Title")
                .content("Post Content")
                .author("Author")
                .isConcept(false)
                .build();

        String postString = objectMapper.writeValueAsString(post);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postString))
                .andExpect(status().isCreated());

        assertEquals(1, postRepository.findAll().size());
    }

    @Test
    public void testUpdatePost() throws Exception {
        Post post = Post.builder()
                .title("Post Title")
                .content("Post Content")
                .author("Author")
                .isConcept(false)
                .build();

        postRepository.save(post);

        Post updatePost = Post.builder()
                .title("Update Title")
                .content("Update Content")
                .author("Update Author")
                .isConcept(false)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/post/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePost)))
                        .andExpect(status().isOk());

        Optional<Post> updatedPost = postRepository.findById(post.getId());

        assertTrue(updatedPost.isPresent());
        assertEquals(updatePost.getAuthor(), updatedPost.get().getAuthor());
        assertEquals(updatePost.getTitle(), updatedPost.get().getTitle());
        assertEquals(updatePost.getContent(), updatedPost.get().getContent());
    }

    @Test
    public void testGetPostById() throws Exception {
        Post post = Post.builder()
                .title("Post Title")
                .content("Post Content")
                .author("Author")
                .isConcept(false)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/" + savedPost.getId()))
                .andExpect(status().isOk());

        Optional<Post> retrievedPost = postRepository.findById(savedPost.getId());

        assertTrue(retrievedPost.isPresent());
        assertEquals(post.getId(), retrievedPost.get().getId());
    }

    @Test
    public void getNotificationsByAuthor() throws Exception {
        Notification notificationFirst = Notification.builder()
                .author("Author1")
                .message("Message1")
                .receiver("Receiver1")
                .build();

        Notification notificationSecond = Notification.builder()
                .author("Author1")
                .message("Message2")
                .receiver("Receiver2")
                .build();

        Notification savedNotificationFirst = notificationRepository.save(notificationFirst);
        Notification savedNotificationSecond = notificationRepository.save(notificationSecond);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/notification/" + notificationFirst.getAuthor()))
                .andExpect(status().isOk());

        List<Notification> retrievedNotifications = notificationRepository.findAll();

        assertEquals(2, notificationRepository.findAll().size());
        assertEquals(savedNotificationFirst.getId(), retrievedNotifications.get(0).getId());
        assertEquals(savedNotificationSecond.getId(), retrievedNotifications.get(1).getId());
    }

    @Test
    public void testGetAuthorByPostId() throws Exception {
        Post post = Post.builder()
                .title("Post Title")
                .content("Post Content")
                .author("Author")
                .isConcept(false)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/author/" + savedPost.getId()))
                .andExpect(status().isOk());

        Optional<Post> retrievedPost = postRepository.findById(savedPost.getId());

        assertTrue(retrievedPost.isPresent());
        assertEquals(post.getAuthor(), retrievedPost.get().getAuthor());
    }

    @Test
    public void testReceiveNotifications() throws Exception {
        Notification notification = Notification.builder()
                .author("Author1")
                .message("Message1")
                .receiver("Receiver1")
                .build();

        String notificationString = objectMapper.writeValueAsString(notification);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post/notification", notification)
                .contentType(MediaType.APPLICATION_JSON)
                .content(notificationString))
                .andExpect(status().isCreated());

        assertEquals(1, notificationRepository.findAll().size());
    }
//    @Test
//    public void testGetFilteredPosts() throws Exception {
//        Post post1 = Post.builder()
//                .author("Author1")
//                .content("Content1")
//                .title("Title1")
//                .status(PostStatus.APPROVED)
//                .isConcept(false)
//                .build();
//
//        Post post2 = Post.builder()
//                .author("Author2")
//                .content("Content2")
//                .title("Title2")
//                .status(PostStatus.PENDING)
//                .isConcept(true)
//                .build();
//
//        postRepository.save(post1);
//        postRepository.save(post2);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/filtered")
//                        .param("author", "Author1")
//                        .param("content", "Content1")
//                        .param("status", "APPROVED"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].author").value("Author1"))
//                .andExpect(jsonPath("$[0].content").value("Content1"))
//                .andExpect(jsonPath("$[0].status").value("PUBLISHED"));
//    }
}

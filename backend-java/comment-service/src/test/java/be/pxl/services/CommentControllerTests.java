package be.pxl.services;

import be.pxl.services.controller.request.CommentRequest;
import be.pxl.services.domain.Comment;
import be.pxl.services.repository.CommentRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class CommentControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

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
        commentRepository.deleteAll();
    }
    @Test
    public void testGetAllCommentsByPostId_withUserRole() throws Exception {
        Long postId = 1L;
        Comment comment = Comment.builder()
                .postId(postId)
                .author("User")
                .content("Test comment")
                .build();
        commentRepository.save(comment);

        mockMvc.perform(get("/api/comment/" + postId)
                        .header("Role", "user"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllCommentsByPostId_withInvalidRole() throws Exception {
        mockMvc.perform(get("/api/comment/1")
                        .header("Role", "guest"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAddCommentToPost_withUserRole() throws Exception {
        Long postId = 1L;
        CommentRequest commentRequest = CommentRequest.builder()
                .author("User")
                .content("New comment")
                .build();

        mockMvc.perform(post("/api/comment/" + postId)
                        .header("Role", "user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated());

        assertEquals(1, commentRepository.findAll().size());
    }

    @Test
    public void testAddCommentToPost_withInvalidRole() throws Exception {
        Long postId = 1L;
        CommentRequest commentRequest = CommentRequest.builder()
                .author("User")
                .content("New comment")
                .build();

        mockMvc.perform(post("/api/comment/" + postId)
                        .header("Role", "guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isForbidden());

        assertEquals(0, commentRepository.findAll().size());
    }

    @Test
    public void testUpdateComment_withUserRole() throws Exception {
        Comment comment = Comment.builder()
                .author("User")
                .content("Original content")
                .postId(1L)
                .build();
        commentRepository.save(comment);

        CommentRequest updatedCommentRequest = CommentRequest.builder()
                .author("User")
                .content("Updated content")
                .build();

        mockMvc.perform(put("/api/comment/" + comment.getId())
                        .header("Role", "user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCommentRequest)))
                .andExpect(status().isOk());

        Comment updatedComment = commentRepository.findById(comment.getId()).orElseThrow();
        assertEquals("Updated content", updatedComment.getContent());
    }

    @Test
    public void testUpdateComment_withInvalidRole() throws Exception {
        Comment comment = Comment.builder()
                .author("User")
                .content("Original content")
                .postId(1L)
                .build();
        commentRepository.save(comment);

        CommentRequest updatedCommentRequest = CommentRequest.builder()
                .author("User")
                .content("Updated content")
                .build();

        mockMvc.perform(put("/api/comment/" + comment.getId())
                        .header("Role", "guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCommentRequest)))
                .andExpect(status().isForbidden());

        Comment unchangedComment = commentRepository.findById(comment.getId()).orElseThrow();
        assertEquals("Original content", unchangedComment.getContent());
    }

    @Test
    public void testDeleteComment_withUserRole() throws Exception {
        Comment comment = Comment.builder()
                .author("User")
                .content("Test comment")
                .postId(1L)
                .build();
        commentRepository.save(comment);

        mockMvc.perform(delete("/api/comment/" + comment.getId())
                        .header("Role", "user"))
                .andExpect(status().isOk());

        assertEquals(0, commentRepository.findAll().size());
    }

    @Test
    public void testDeleteComment_withInvalidRole() throws Exception {
        Comment comment = Comment.builder()
                .author("User")
                .content("Test comment")
                .postId(1L)
                .build();
        commentRepository.save(comment);

        mockMvc.perform(delete("/api/comment/" + comment.getId())
                        .header("Role", "guest"))
                .andExpect(status().isForbidden());

        assertEquals(1, commentRepository.findAll().size());
    }
}

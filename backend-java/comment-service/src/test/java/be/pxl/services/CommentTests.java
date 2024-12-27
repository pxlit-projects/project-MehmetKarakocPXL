package be.pxl.services;

import be.pxl.services.controller.request.CommentRequest;
import be.pxl.services.domain.Comment;
import be.pxl.services.repository.CommentRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class CommentTests {

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
    public void testDeleteComment() throws Exception {
        Comment comment = Comment.builder()
                .author("Muto")
                .content("This is a comment")
                .postId(1L)
                .build();

        commentRepository.save(comment);

        mockMvc.perform(delete("/api/comment/" + comment.getId()))
                .andExpect(status().isOk());

        assertEquals(0, commentRepository.findAll().size());
    }

    @Test
    public void testAddCommentToPost() throws Exception {
        Comment comment = Comment.builder()
                .author("Muto")
                .content("This is a comment")
                .postId(1L)
                .build();

        String commentString = objectMapper.writeValueAsString(comment);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/" + comment.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentString))
                .andExpect(status().isCreated());

        assertEquals(1, commentRepository.findAll().size());
    }

    @Test
    public void testGetAllCommentsByPostId() throws Exception {
        Comment comment = Comment.builder()
                .author("Muto")
                .content("This is a comment")
                .postId(1L)
                .build();

        Comment savedComment = commentRepository.save(comment);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/comment/" + savedComment.getId()))
                .andExpect(status().isOk());

        Optional<Comment> retrievedComment = commentRepository.findById(savedComment.getId());

        assertTrue(retrievedComment.isPresent());
        assertEquals(comment.getId(), retrievedComment.get().getId());
    }

    @Test
    public void testUpdateComment() throws Exception {
        Comment commentFirst = Comment.builder()
                .author("Muto")
                .content("This is a comment")
                .postId(1L)
                .createdDate(null)
                .build();

        commentRepository.save(commentFirst);

        Comment commentSecond = Comment.builder()
                .author("updated")
                .content("updated")
                .postId(1L)
                .build();

        mockMvc.perform(put("/api/comment/" + commentFirst.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentSecond)))
                .andExpect(status().isOk());

        Optional<Comment> updatedComment = commentRepository.findById(commentFirst.getId());

        assertTrue(updatedComment.isPresent());
        assertEquals(updatedComment.get().getContent(), commentSecond.getContent());
        assertEquals(updatedComment.get().getAuthor(), commentSecond.getAuthor());
        assertEquals(updatedComment.get().getPostId(), commentSecond.getPostId());

    }


}

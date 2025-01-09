package be.pxl.services;


import be.pxl.services.controller.request.ReviewRequest;
import be.pxl.services.domain.Review;
import be.pxl.services.repository.ReviewRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ReviewControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewRepository reviewRepository;

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
        reviewRepository.deleteAll();
    }

    @Test
    public void testAddReview_withAdminRole() throws Exception {
        ReviewRequest reviewRequest = ReviewRequest.builder()
                .postId(1L)
                .author("User")
                .content("Excellent post!")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/review")
                        .header("Role", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated());

        assertEquals(1, reviewRepository.findAll().size());
    }

    @Test
    public void testAddReview_withNonAdminRole() throws Exception {
        ReviewRequest reviewRequest = ReviewRequest.builder()
                .postId(1L)
                .author("User")
                .content("Excellent post!")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/review")
                        .header("Role", "user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isForbidden());

        assertEquals(0, reviewRepository.findAll().size());
    }

    @Test
    public void testGetReviews_withAdminRole() throws Exception {
        Review review = Review.builder()
                .postId(1L)
                .content("Great post!")
                .build();
        reviewRepository.save(review);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/review")
                        .header("Role", "admin"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetReviews_withUserRole() throws Exception {
        Review review = Review.builder()
                .postId(1L)
                .content("Good post!")
                .build();
        reviewRepository.save(review);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/review")
                        .header("Role", "user"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetReviews_withInvalidRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/review")
                        .header("Role", "guest"))
                .andExpect(status().isForbidden());
    }
}

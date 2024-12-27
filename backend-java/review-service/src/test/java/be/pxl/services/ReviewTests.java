package be.pxl.services;


import be.pxl.services.domain.Review;
import be.pxl.services.repository.ReviewRepository;
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
public class ReviewTests {

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
    public void testGetReviewByPostId() throws Exception {
        Review review = Review.builder()
                .author("Muto")
                .content("Great post")
                .isApproved(true)
                .build();

        Review savedReview = reviewRepository.save(review);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/review/" + savedReview.getId()))
                .andExpect(status().isOk());

        Optional<Review> retrievedReview = reviewRepository.findById(savedReview.getId());

        assertTrue(retrievedReview.isPresent());
        assertEquals(review.getId(), retrievedReview.get().getId());

    }

    @Test
    public void testAddReview() throws Exception {
        Review review = Review.builder()
                .author("Muto")
                .content("Great post")
                .isApproved(true)
                .postId(1L)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isCreated());

        assertEquals(1, reviewRepository.findAll().size());
    }

}

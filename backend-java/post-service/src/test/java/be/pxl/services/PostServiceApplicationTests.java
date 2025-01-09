package be.pxl.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceApplicationTests {

    @Test
    void mainMethodShouldRunUsingMockedStatic() {
        try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() ->
                    SpringApplication.run(PostServiceApplication.class, new String[] {})
            ).thenReturn(null);
            PostServiceApplication.main(new String[] {});
            mockedSpringApplication.verify(() ->
                            SpringApplication.run(PostServiceApplication.class, new String[] {}),
                    times(1)
            );
        }
    }

    @Test
    void contextLoadsSuccessfully() {
        // Ensure that the Spring Boot application context loads without errors
        // No need to mock here as it validates the configuration and beans
    }
}


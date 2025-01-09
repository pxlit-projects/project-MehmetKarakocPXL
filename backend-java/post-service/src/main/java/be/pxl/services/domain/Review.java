package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Data
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long postId;
    private String author;
    private String content;
    private boolean isApproved;

    public Review() {

    }

    public Review(Long id, Long postId, String author, String content, boolean isApproved) {
        this.id = id;
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.isApproved = isApproved;
    }
}

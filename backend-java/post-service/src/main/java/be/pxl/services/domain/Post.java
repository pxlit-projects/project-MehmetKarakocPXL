package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="posts")
@Data
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String author;
    private String title;
    private String content;
    private Boolean isConcept;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    public Post() {

    }

    public Post(Long id, String author, String title, String content, Boolean isConcept, PostStatus status, LocalDateTime createdDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.isConcept = isConcept;
        this.status = status;
        this.createdDate = createdDate;
    }
}

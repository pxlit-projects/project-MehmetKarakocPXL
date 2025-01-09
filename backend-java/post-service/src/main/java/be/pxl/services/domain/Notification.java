package be.pxl.services.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="notifications")
@Data
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String message;
    private String author;
    private String receiver;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    public Notification() {

    }

    public Notification(Long id, String message, String author, String receiver, LocalDateTime createdDate) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.receiver = receiver;
        this.createdDate = createdDate;
    }
}

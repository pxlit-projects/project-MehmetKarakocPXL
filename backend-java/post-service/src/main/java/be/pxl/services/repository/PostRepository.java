package be.pxl.services.repository;

import be.pxl.services.controller.response.PostResponse;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE " +
            "(:content IS NULL OR p.content LIKE %:content%) AND " +
            "(:author IS NULL OR p.author LIKE %:author%) AND " +
            "(:date IS NULL OR FUNCTION('DATE', p.createdDate) = :date) AND" +
            "(:status IS NULL OR p.status = :status)")
    List<Post> findByFilters(@Param("content") String content,
                             @Param("author") String author,
                             @Param("date") LocalDate date,
                            @Param("status") PostStatus status);
}

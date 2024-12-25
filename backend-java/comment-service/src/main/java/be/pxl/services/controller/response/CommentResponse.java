package be.pxl.services.controller.response;

import java.time.LocalDateTime;

public record CommentResponse (Long id, Long postId, String author, String content, LocalDateTime createdDate) {
}

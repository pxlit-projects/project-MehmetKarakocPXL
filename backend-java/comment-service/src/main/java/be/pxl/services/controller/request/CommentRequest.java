package be.pxl.services.controller.request;

import java.time.LocalDateTime;

public record CommentRequest (Long postId, String author, String content) {
}

package be.pxl.services.controller.request;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record CommentRequest (Long postId, String author, String content) {
}

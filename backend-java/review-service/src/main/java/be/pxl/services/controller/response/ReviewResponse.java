package be.pxl.services.controller.response;

import java.time.LocalDateTime;

public record ReviewResponse(Long id, String content, Long postId, String author, boolean isApproved) {
}

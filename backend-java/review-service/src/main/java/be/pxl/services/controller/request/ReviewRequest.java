package be.pxl.services.controller.request;

import java.time.LocalDateTime;

public record ReviewRequest(Long postId, String content, String author, boolean isApproved) {
}

package be.pxl.services.controller.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewRequest(Long postId, String content, String author, boolean isApproved) {
}

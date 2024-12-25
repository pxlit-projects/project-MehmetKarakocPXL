package be.pxl.services.controller.response;

import be.pxl.services.domain.PostStatus;

import java.time.LocalDateTime;

public record PostResponse(Long id, String author, String title, String content, boolean isConcept, PostStatus status, LocalDateTime createdDate) {
}

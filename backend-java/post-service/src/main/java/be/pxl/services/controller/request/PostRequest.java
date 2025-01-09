package be.pxl.services.controller.request;

import be.pxl.services.domain.PostStatus;
import lombok.Builder;

@Builder
public record PostRequest(String author, String title, String content, boolean isConcept, PostStatus status) {
}

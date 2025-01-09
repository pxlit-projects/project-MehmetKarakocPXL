package be.pxl.services.controller.request;

import lombok.Builder;

@Builder
public record CommentRequest (Long postId, String author, String content) {
}

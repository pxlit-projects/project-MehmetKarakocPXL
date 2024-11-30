package be.pxl.services.controller.response;

public record PostResponse(Long id, String author, String title, String content, boolean isConcept) {
}

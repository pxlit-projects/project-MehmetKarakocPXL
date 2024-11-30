package be.pxl.services.controller.request;

public record PostRequest(String author, String title, String content, boolean isConcept) {
}

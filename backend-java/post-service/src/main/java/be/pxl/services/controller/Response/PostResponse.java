package be.pxl.services.controller.Response;

import java.time.LocalDate;

public record PostResponse(Long id, String author, String title, String content, boolean isConcept) {
}

package be.pxl.services.controller.Request;

import java.time.LocalDate;

public record PostRequest(String author, String title, String content, boolean isConcept) {
}

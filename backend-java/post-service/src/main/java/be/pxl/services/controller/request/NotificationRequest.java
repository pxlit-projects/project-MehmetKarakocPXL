package be.pxl.services.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record NotificationRequest(String message, String author, String receiver) {
}

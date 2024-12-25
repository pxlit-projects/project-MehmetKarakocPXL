package be.pxl.services.client;

import be.pxl.services.controller.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "post-service")
public interface NotificationClient {

    @PostMapping("/api/post/notification")
    void sendNotification(@RequestBody NotificationRequest notificationRequest);

    @GetMapping("/api/post/author/{postId}")
    String getAuthor(@PathVariable("postId") Long postId);
}


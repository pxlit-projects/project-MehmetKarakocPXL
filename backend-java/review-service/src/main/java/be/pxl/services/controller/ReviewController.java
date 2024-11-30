package be.pxl.services.controller;

import be.pxl.services.client.NotificationClient;
import be.pxl.services.services.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class ReviewController {
    private final ReviewService reviewService;
    private final NotificationClient notificationClient;
    //US-8

}

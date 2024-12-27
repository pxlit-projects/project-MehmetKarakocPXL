package be.pxl.services.services;

import be.pxl.services.controller.request.NotificationRequest;
import be.pxl.services.controller.response.PostResponse;
import be.pxl.services.domain.Notification;
import be.pxl.services.controller.request.PostRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IPostService {

    PostResponse getPostById(Long postId);

    void addPost(PostRequest postRequest);

    void updatePost(Long postId, PostRequest postRequest);

    List<PostResponse> getFilteredPosts(String content, String author, LocalDate date, String status);

    String getAuthorByPostId(Long postId);

    void saveNotification(NotificationRequest notificationRequest);

    List<Notification> getNotificationsByAuthor(String author);
}
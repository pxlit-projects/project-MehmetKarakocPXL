package be.pxl.services.services;

import be.pxl.services.controller.request.NotificationRequest;
import be.pxl.services.controller.response.PostResponse;
import be.pxl.services.domain.Notification;
import be.pxl.services.domain.Post;
import be.pxl.services.controller.request.PostRequest;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.Review;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.repository.NotificationRepository;
import be.pxl.services.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = "myQueue")
    public void listenReview(Review review) {
        Post post = postRepository.findById(review.getPostId()).orElseThrow(() -> new NotFoundException("No post with id [" + review.getPostId() + "]"));
        if (review.isApproved()) {
            post.setStatus(PostStatus.APPROVED);
        } else {
            post.setStatus(PostStatus.REJECTED);
            post.setIsConcept(true);
        }
        postRepository.save(post);
    }

    @Override
    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("No post with id [" + postId + "]"));
        return new PostResponse(post.getId(), post.getAuthor(), post.getTitle(), post.getContent(), post.getIsConcept(), post.getStatus(), post.getCreatedDate());
    }

    @Override
    public void addPost(PostRequest postRequest) {
        Post post = new Post();
        post.setAuthor(postRequest.author());
        post.setTitle(postRequest.title());
        post.setContent(postRequest.content());
        post.setIsConcept(postRequest.isConcept());
        post.setStatus(postRequest.status());
        postRepository.save(post);
    }

    @Override
    public void updatePost(Long postId, PostRequest postRequest){
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("No post with id [" + postId + "]"));
        post.setAuthor(postRequest.author());
        post.setTitle(postRequest.title());
        post.setContent(postRequest.content());
        post.setIsConcept(postRequest.isConcept());
        post.setStatus(PostStatus.PENDING);
        postRepository.save(post);
    }

    @Override
    public List<PostResponse> getFilteredPosts(String content, String author, LocalDate date, String status) {
        PostStatus postStatus = (status != null) ? PostStatus.valueOf(status.toUpperCase()) : null;
        List<Post> posts = postRepository.findByFilters(content, author, date, postStatus);
        return getPostResponses(posts);
    }

    private List<PostResponse> getPostResponses(List<Post> posts) {
        List <PostResponse> postResponses = new ArrayList<>();
        for (Post post : posts) {
            PostResponse PostResponse = new PostResponse(post.getId(), post.getAuthor(), post.getTitle(), post.getContent(), post.getIsConcept(), post.getStatus(), post.getCreatedDate());
            postResponses.add(PostResponse);
        }
        return postResponses;
    }

    @Override
    public String getAuthorByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post author not found with ID: " +  postId));
        return post.getAuthor();
    }

    @Override
    public void saveNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        notification.setAuthor(notificationRequest.author());
        notification.setMessage(notificationRequest.message());
        notification.setReceiver(notificationRequest.receiver());
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByReceiver(String receiver) {
        return notificationRepository.findByReceiverOrderByCreatedDateDesc(receiver);
    }

}

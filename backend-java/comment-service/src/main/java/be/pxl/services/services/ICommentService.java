package be.pxl.services.services;

import be.pxl.services.controller.request.CommentRequest;
import be.pxl.services.controller.response.CommentResponse;

import java.util.List;

public interface ICommentService {
    List<CommentResponse> getAllCommentsByPostId(Long postId);

    void addCommentToPost(Long postId, CommentRequest commentRequest);

    void updateComment(Long commentId, CommentRequest commentRequest);

    void deleteComment(Long commentId);
}

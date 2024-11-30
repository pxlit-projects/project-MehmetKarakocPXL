package be.pxl.services.services;

import be.pxl.services.domain.Comment;

import java.util.List;

public interface ICommentSerivce {
    List<Comment> getAllCommentsByPostId(Long postId);
}

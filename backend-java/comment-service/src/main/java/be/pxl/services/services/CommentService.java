package be.pxl.services.services;

import be.pxl.services.controller.request.CommentRequest;
import be.pxl.services.controller.response.CommentResponse;
import be.pxl.services.domain.Comment;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;

    @Override
    public List<CommentResponse>  getAllCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId).stream().toList();
        List<CommentResponse> commentResponses= new ArrayList<>();
        for (Comment comment:comments
        ) {
            commentResponses.add(new CommentResponse(comment.getId(), comment.getPostId(), comment.getAuthor(), comment.getContent(), comment.getCreatedDate()));
        }
        return commentResponses;
    }

    @Override
    public void addCommentToPost(Long postId, CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setAuthor(commentRequest.author());
        comment.setContent(commentRequest.content());
        commentRepository.save(comment);
    }

    @Override
    public void updateComment(Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("No comment with id [" + commentId + "]"));
        comment.setAuthor(commentRequest.author());
        comment.setContent(commentRequest.content());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("No comment with id [" + commentId + "]"));
        commentRepository.delete(comment);
    }

}

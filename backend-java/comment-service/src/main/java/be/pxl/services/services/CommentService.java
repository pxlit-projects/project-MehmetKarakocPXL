package be.pxl.services.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentSerivce {
    private final CommentRepository commentRepository;

    @Override
    public List<Comment>  getAllCommentsByPostId(Long postId) {
        return commentRepository.findCommentsByPostId(postId).stream().toList();
    }
}

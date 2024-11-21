package be.pxl.services.services;

import be.pxl.services.controller.Response.PostResponse;
import be.pxl.services.domain.Post;
import be.pxl.services.controller.Request.PostRequest;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Long addPost(PostRequest postRequest) {
        Post post = new Post();
        post.setAuthor(postRequest.author());
        post.setTitle(postRequest.title());
        post.setContent(postRequest.content());
        post.setConcept(postRequest.isConcept());
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public PostResponse updatePost(Long postId, PostRequest postRequest){
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("No post with id [" + postId + "]"));
        post.setAuthor(postRequest.author());
        post.setTitle(postRequest.title());
        post.setContent(postRequest.content());
        post.setConcept(postRequest.isConcept());
        postRepository.save(post);
        return new PostResponse(post.getId(), post.getAuthor(), post.getTitle(), post.getContent(), post.isConcept());
    }

    @Override
    public List<Post> getAllPostsSorted(String sortBy) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy); // Default to ascending order
        
        return postRepository.findAll(sort);
    }


}

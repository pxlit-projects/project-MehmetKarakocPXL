package be.pxl.services.services;

import be.pxl.services.controller.response.PostResponse;
import be.pxl.services.domain.Post;
import be.pxl.services.controller.request.PostRequest;

import java.util.List;

public interface IPostService {

    List<Post> getAllPosts();

    Long addPost(PostRequest postRequest);

    PostResponse updatePost(Long postId, PostRequest postRequest);

    List<Post> getAllPostsSorted(String sortBy);
}
package com.tpsolution.animestore.service;

import com.tpsolution.animestore.entity.Post;
import com.tpsolution.animestore.exception.ResourceNotFoundException;
import com.tpsolution.animestore.payload.AddPostRequest;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.repository.PostRepository;
import com.tpsolution.animestore.service.imp.PostServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService implements PostServiceImp {

    @Autowired
    private PostRepository postRepository;
    @Override
    public DataResponse getAllPost() {
        return DataResponse.ok(postRepository.findAll());
    }

    @Override
    public DataResponse findPostById(Long id) {
        return DataResponse.ok(postRepository.findById(id));
    }

    @Override
    public DataResponse createPost(AddPostRequest addPostRequest) {
        Post post = buildPostDTO(addPostRequest);
        post.setCreatedAt(LocalDateTime.now());
        return DataResponse.ok(postRepository.save(post));
    }

    @Override
    public DataResponse updatePost(Long id, AddPostRequest postDetails) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setAuthor(postDetails.getAuthor());
        post.setUpdatedAt(LocalDateTime.now());

        return DataResponse.ok(postRepository.save(post));
    }

    @Override
    public DataResponse deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postRepository.delete(post);
        return DataResponse.ok("");
    }

    private Post buildPostDTO(AddPostRequest addPostRequest) {
        Post post = new Post();
        post.setTitle(addPostRequest.getTitle());
        post.setContent(addPostRequest.getContent());
        post.setAuthor(addPostRequest.getAuthor());
        return post;
    }
}

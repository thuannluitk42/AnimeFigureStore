package com.tpsolution.animestore.controller;

import com.tpsolution.animestore.entity.Post;
import com.tpsolution.animestore.exception.ResourceNotFoundException;
import com.tpsolution.animestore.payload.AddPostRequest;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.service.imp.PostServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostServiceImp postService;

    @GetMapping
    public ResponseEntity<DataResponse> getAllPosts() {
        return ResponseEntity.ok().body(postService.getAllPost());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok().body(postService.findPostById(id));
    }

    @PostMapping
    public ResponseEntity<DataResponse> createPost(@RequestBody AddPostRequest post) {
        return ResponseEntity.ok().body(postService.createPost(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse> updatePost(@PathVariable Long id, @RequestBody AddPostRequest postDetails) {
        return ResponseEntity.ok().body(postService.updatePost(id, postDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> deletePost(@PathVariable Long id) {
        return ResponseEntity.ok().body(postService.deletePost(id));
    }
}

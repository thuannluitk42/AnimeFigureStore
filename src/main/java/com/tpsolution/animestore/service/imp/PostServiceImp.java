package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.AddPostRequest;
import com.tpsolution.animestore.payload.DataResponse;

public interface PostServiceImp {
    DataResponse getAllPost();
    DataResponse findPostById(Long id);
    DataResponse createPost(AddPostRequest addPostRequest);
    DataResponse updatePost(Long post_id, AddPostRequest post);
    DataResponse deletePost(Long post_id);
}

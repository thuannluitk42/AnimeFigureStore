package com.tpsolution.animestore.payload;

import lombok.Data;

@Data
public class AddPostRequest {
    private String title;
    private String content;
    private String author;
}

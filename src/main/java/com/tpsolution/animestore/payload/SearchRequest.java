package com.tpsolution.animestore.payload;
import lombok.Data;

@Data
public class SearchRequest {
    private String search;
    private int page;
    private int size;
}

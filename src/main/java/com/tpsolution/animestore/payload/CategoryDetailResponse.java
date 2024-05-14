package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryDetailResponse {
    private int categoryId;
    private String categoryName;
}

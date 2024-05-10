package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryDetailResponse {
    private UUID categoryId;
    private String categoryName;
}

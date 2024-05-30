package com.tpsolution.animestore.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private List<Integer> categoryId;
    private String categoryName;
}

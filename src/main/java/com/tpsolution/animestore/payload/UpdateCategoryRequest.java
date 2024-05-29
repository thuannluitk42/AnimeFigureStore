package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateCategoryRequest {
    @NotEmpty()
    private String categoryName;
    @NotEmpty()
    private String categoryId;
}

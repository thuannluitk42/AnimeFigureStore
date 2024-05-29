package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddCategoryRequest {
    @NotEmpty()
    private String categoryName;
}

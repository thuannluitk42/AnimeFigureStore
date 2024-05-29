package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse extends PagingResponse{
    private List<CategoryDetailResponse> list;
}

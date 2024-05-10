package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponse extends PagingResponse {
    private List<ProductDetailResponse> list;
}

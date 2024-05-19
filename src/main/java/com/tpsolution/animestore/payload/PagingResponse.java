package com.tpsolution.animestore.payload;

import lombok.Data;

@Data
public class PagingResponse {
    private int totalPage;
    private int currentPage;
    private Long totalElement;
}

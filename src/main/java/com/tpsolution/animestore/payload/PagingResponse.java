package com.tpsolution.animestore.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingResponse {
    private int totalPage;
    private int currentPage;
    private Long totalElement;
}

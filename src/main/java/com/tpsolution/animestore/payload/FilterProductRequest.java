package com.tpsolution.animestore.payload;

import lombok.Data;

@Data
public class FilterProductRequest {
    private int idCategory;
    private int optionPrice;
    private int sortBy;
    private int page;
    private int size;
}

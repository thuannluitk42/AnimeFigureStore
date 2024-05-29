package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.List;

@Data
public class ProductRes {
    private int categoryId;
    private String categoryName;
    private List <ProductResHP> productResHPS;

}

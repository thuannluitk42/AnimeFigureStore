package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.UUID;
@Data
public class ProductDetailResponse {
    private UUID productId;
    private String product_name;
    private double product_price;
    private String images;
    private int product_quantity ;
    private String product_description ;
    private String product_discount ;
    private int category_id;
}

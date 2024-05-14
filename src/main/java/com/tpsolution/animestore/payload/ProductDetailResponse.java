package com.tpsolution.animestore.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Data
@Getter
@Setter
public class ProductDetailResponse {
    private int productId;
    private String product_name;
    private double product_price;
    private String images;
    private int product_quantity ;
    private String product_description ;
    private String product_discount ;
    private int category_id;
}

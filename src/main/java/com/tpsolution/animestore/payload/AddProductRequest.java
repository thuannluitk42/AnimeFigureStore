package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddProductRequest {
    private String product_name;
    private double product_price;
    private String urlImage;
    private int product_quantity ;
    private String product_description ;
    private String product_discount ;
    private int category_id;
}

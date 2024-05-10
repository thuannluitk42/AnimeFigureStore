package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateProductRequest {
    @NotEmpty()
    private String productId;
    @NotEmpty()
    private String product_name;
    @NotEmpty()
    private double product_price;
    @NotEmpty()
    private String images;
    @NotEmpty()
    private int product_quantity ;
    @NotEmpty()
    private String product_description ;
    @NotEmpty()
    private String product_discount ;
    @NotEmpty()
    private int category_id;
}

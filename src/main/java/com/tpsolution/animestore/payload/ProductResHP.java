package com.tpsolution.animestore.payload;

import lombok.Data;

@Data
public  class ProductResHP {
    private int productId;
    private double productPrice;
    private String productName;
    private String productAvatar;
}
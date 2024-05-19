package com.tpsolution.animestore.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailResponse {
    private int productId;
    private String product_name;
    private double product_price;
    private String images;
    private int product_quantity ;
    private String product_description ;
    private String product_discount ;
    private int category_id;
    private boolean isDeleted;

}

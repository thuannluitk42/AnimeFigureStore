package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateProductRequest extends AddProductRequest{
    @NotEmpty()
    private String productId;

}

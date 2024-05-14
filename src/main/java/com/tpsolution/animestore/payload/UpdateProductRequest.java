package com.tpsolution.animestore.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateProductRequest extends AddProductRequest{
    // replace Optional<String> ? Optional<String> productId
    @NotEmpty()
    private String productId;

}

package com.tpsolution.animestore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDTO {
    private int productId;
    private int amount;
    private double unitPrice;
    private double subTotal;
}

package com.tpsolution.animestore.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private int productId;
    private int amount;
    private double unitPrice;
    private double subTotal;
}

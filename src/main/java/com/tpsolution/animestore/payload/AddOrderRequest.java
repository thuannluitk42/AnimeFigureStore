package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class AddOrderRequest {
    private UUID userId;
    private double totalBill;
}

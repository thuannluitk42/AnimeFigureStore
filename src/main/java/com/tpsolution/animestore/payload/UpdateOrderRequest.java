package com.tpsolution.animestore.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateOrderRequest {
    private UUID orderId;
    private UUID userId;
    private double totalBill;
    private String deliveryAddress;
    private int paymentOption;// loai thanh toan
    private int paymentStatus; // trang thai thanh toan
    private int vnpayTransactionId;
}

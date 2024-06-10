package com.tpsolution.animestore.payload;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddVoucherRequest {
    private String voucherCode;
    private String description;
    private Double discountValue;
    private LocalDate expiryDate;
    private Integer maxUsage;
    private boolean active;
}

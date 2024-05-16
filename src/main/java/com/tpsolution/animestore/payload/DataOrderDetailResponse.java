package com.tpsolution.animestore.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tpsolution.animestore.entity.Order;
import com.tpsolution.animestore.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataOrderDetailResponse {
    private String productName;
    private int amount;
    private double unitPrice;
    private double subTotal;
}

package com.tpsolution.animestore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class KeyOrdersDetail implements Serializable {

    @Column(name = "order_id")
    private int orderId;
    @Column(name = "food_id")
    private int foodId;
}

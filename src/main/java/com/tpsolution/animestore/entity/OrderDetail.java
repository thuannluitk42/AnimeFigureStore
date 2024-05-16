package com.tpsolution.animestore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders_detail")
public class OrderDetail implements Serializable {

    @EmbeddedId
    KeyOrdersDetail keys;

//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Order order;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;

    @Column(name = "amount")
    private int amount;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "sub_total")
    private double subTotal;
}

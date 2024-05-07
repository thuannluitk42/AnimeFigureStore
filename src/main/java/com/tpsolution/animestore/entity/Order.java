package com.tpsolution.animestore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", length = 11, nullable = false, unique = true)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(name = "total")
    private double total;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "payment_option")
    private int paymentOption;

    @Column(name = "payment_status")
    private int paymentStatus;

    @Column(name = "vnpay_transaction_id")
    private int vnpayTransactionId;

    @Column(name = "created_date")
    private Date createdDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();
}

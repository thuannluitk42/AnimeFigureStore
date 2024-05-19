package com.tpsolution.animestore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
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

//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<OrderDetail> orderDetails = new HashSet<>();
}

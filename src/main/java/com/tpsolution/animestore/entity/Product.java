package com.tpsolution.animestore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", length = 11, nullable = false, unique = true)
    private int productId;

    @Column(name = "product_name", length = 255, nullable = false)
    private String productName;

    @Column(name = "product_price", nullable = false)
    private double productPrice;

    @Column(name = "product_images")
    private String productImages;

    @Column(name = "product_quantity", nullable = false)
    private int productQuantity;

    @Column(name = "product_description", nullable = true)
    private String productDescription;

    @Column(name = "discount", nullable = true)
    private String discount;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "is_deleted",nullable = true)
    private boolean isDeleted;

    @Column(name = "created_date",nullable = true)
    private Date createdDate;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<OrderDetail> orderDetails = new HashSet<>();
}

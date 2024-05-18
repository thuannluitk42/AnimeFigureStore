package com.tpsolution.animestore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", length = 11, nullable = false, unique = true)
    private int categoryId;

    @Column(name = "category_name", length = 255, nullable = false)
    private String categoryName;

    @Column(name = "is_deleted",nullable = true)
    private boolean isDeleted;

    @Column(name = "created_date",nullable = true)
    private Date createdDate;
}

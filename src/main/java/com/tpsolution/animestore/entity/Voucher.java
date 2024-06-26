package com.tpsolution.animestore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vouchers")
@RedisHash("Voucher")
public class Voucher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long id;

    @Column(name = "voucher_code")
    private String voucherCode;
    @Column(name = "description")
    private String description;
    @Column(name = "discount_value")
    private Double discountValue;
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    @Column(name = "max_usage")
    private Integer maxUsage;

    @Column(name = "active")
    private boolean active;  // Field to indicate if voucher is active
    @Column(name = "usage_count")
    private Integer usageCount;  // Field to track the number of times the voucher has been used

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "voucher")
    private Set<Order> orders = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

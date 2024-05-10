package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderCriteriaRepository extends JpaSpecificationExecutor<Order>, JpaRepository<Order, Long> {
}

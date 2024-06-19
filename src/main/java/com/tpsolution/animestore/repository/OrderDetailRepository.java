package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, UUID>, JpaSpecificationExecutor<OrderDetail> {
    @Query("SELECT o FROM OrderDetail o WHERE o.keys.orderId = :orderId")
    List<OrderDetail> findOrderDetailByOrderId (int orderId);

    @Query("SELECT COUNT(od.keys.productId) FROM OrderDetail od JOIN Product p ON od.keys.productId = p.productId GROUP BY p.category.categoryId")
    long countProductsSoldPerCategory();
}

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

    @Query("SELECT YEAR(o.createdDate) AS year, MONTH(o.createdDate) AS month, SUM(od.amount) AS productsSold " +
            "FROM OrderDetail od JOIN Order o ON od.keys.orderId = o.orderId " +
            "WHERE o.paymentStatus = 1 " +
            "GROUP BY YEAR(o.createdDate), MONTH(o.createdDate) " +
            "ORDER BY YEAR(o.createdDate), MONTH(o.createdDate)")
    List<Object[]> findMonthlyProductsSold();

    @Query("SELECT YEAR(o.createdDate) AS year, QUARTER(o.createdDate) AS quarter, SUM(od.amount) AS productsSold " +
            "FROM OrderDetail od JOIN Order o ON od.keys.orderId = o.orderId " +
            "WHERE o.paymentStatus = 1 " +
            "GROUP BY YEAR(o.createdDate), QUARTER(o.createdDate) " +
            "ORDER BY YEAR(o.createdDate), QUARTER(o.createdDate)")
    List<Object[]> findQuarterlyProductsSold();

    @Query("SELECT YEAR(o.createdDate) AS year, SUM(od.amount) AS productsSold " +
            "FROM OrderDetail od JOIN Order o ON od.keys.orderId = o.orderId " +
            "WHERE o.paymentStatus = 1 " +
            "GROUP BY YEAR(o.createdDate) " +
            "ORDER BY YEAR(o.createdDate)")
    List<Object[]> findYearlyProductsSold();
}

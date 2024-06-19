package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    @Query("SELECT o FROM Order o WHERE o.createdDate = :createdDate")
    List<Order> findOrderByCreatedDate(Date createdDate);

    Order findOrderByOrderIdAndPaymentStatus(int orderId, int paymentStatus);
    Order findOrderByOrderId(int orderId);

    @Query("SELECT o FROM Order o WHERE o.paymentOption = :paymentOption")
    List<Order> findOrderByPaymentOption(int paymentOption);

    @Query("SELECT o FROM Order o WHERE o.paymentStatus = :paymentStatus")
    List<Order> findOrderByPaymentStatus(int paymentStatus);

    @Query("SELECT o FROM Order o WHERE o.createdDate = :yesterday ORDER BY o.total DESC LIMIT 4")
    List<Order> get4OrderWithHighestTotalBillYesterday(LocalDate yesterday);
    @Query("SELECT o FROM Order o WHERE o.createdDate = :currentDate ORDER BY o.total DESC LIMIT 2")
    List<Order> get2OrderWithHighestTotalBillToday(LocalDate currentDate);

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.paymentStatus = 1")
    double sumTotalRevenue();

    @Query("SELECT AVG(o.total) FROM Order o WHERE o.paymentStatus = 1")
    double averageOrderValue();

    @Query("SELECT YEAR(o.createdDate) AS year, MONTH(o.createdDate) AS month, SUM(o.total) AS revenue " +
            "FROM Order o WHERE o.paymentStatus = 1 GROUP BY YEAR(o.createdDate), MONTH(o.createdDate) " +
            "ORDER BY YEAR(o.createdDate), MONTH(o.createdDate)")
    List<Object[]> findMonthlyRevenue();

    @Query("SELECT YEAR(o.createdDate) AS year, QUARTER(o.createdDate) AS quarter, SUM(o.total) AS revenue " +
            "FROM Order o WHERE o.paymentStatus = 1 GROUP BY YEAR(o.createdDate), QUARTER(o.createdDate) " +
            "ORDER BY YEAR(o.createdDate), QUARTER(o.createdDate)")
    List<Object[]> findQuarterlyRevenue();

    @Query("SELECT YEAR(o.createdDate) AS year, SUM(o.total) AS revenue " +
            "FROM Order o WHERE o.paymentStatus = 1 GROUP BY YEAR(o.createdDate) " +
            "ORDER BY YEAR(o.createdDate)")
    List<Object[]> findYearlyRevenue();
}

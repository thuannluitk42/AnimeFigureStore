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
    List<Order> get4OrdersWithHighestTotalBillYesterday(LocalDate yesterday);
    @Query("SELECT o FROM Order o WHERE o.createdDate = :currentDate ORDER BY o.total DESC LIMIT 2")
    List<Order> get2OrdersWithHighestTotalBillToday(LocalDate currentDate);
}

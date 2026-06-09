package com.example.ordermanagement.repository;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    @Query("SELECT COALESCE(SUM(o.grandTotal), 0) FROM Order o " +
            "WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate " +
            "AND o.orderStatus = :status")
    BigDecimal calculateTotalRevenue(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate,
                                     @Param("status") OrderStatus status);

    @Query("SELECT COUNT(o) FROM Order o " +
            "WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate")
    long countTotalOrders(@Param("startDate") LocalDateTime startDate,
                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o.orderStatus, COUNT(o) FROM Order o " +
            "WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate " +
            "GROUP BY o.orderStatus")
    List<Object[]> countOrdersGroupedByStatus(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
}
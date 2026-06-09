package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem, String> {
    List<OrderItem> findByOrderId(String orderId);
}

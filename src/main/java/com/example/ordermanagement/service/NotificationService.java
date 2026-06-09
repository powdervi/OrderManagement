package com.example.ordermanagement.service;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.entity.Order;

public interface NotificationService {
    void createNotification(Order order, OrderStatus status);
}

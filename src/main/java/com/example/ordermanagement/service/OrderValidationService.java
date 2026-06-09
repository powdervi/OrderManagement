package com.example.ordermanagement.service;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.common.UserRole;
import com.example.ordermanagement.dto.request.OrderItemReq;
import com.example.ordermanagement.entity.Address;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.PaymentMethod;

import java.util.List;

public interface OrderValidationService {
    void validateDuplicateProducts(List<OrderItemReq> items);
    void validateOrderAccess(Order order);
    void validateTransition(OrderStatus currentStatus, OrderStatus newStatus);
    void validateRolePermission(String userId, OrderStatus currentStatus, OrderStatus newStatus);
}

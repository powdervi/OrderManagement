package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.OrderItemReq;
import java.util.List;

public interface CartCleanupService {
    void removeCartItems(String userId, List<OrderItemReq> items);
}

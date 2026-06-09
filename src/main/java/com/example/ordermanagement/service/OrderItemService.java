package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.OrderItemReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.List;

public interface OrderItemService {
    @Getter
    @AllArgsConstructor
    class ItemMetrics {
        private BigDecimal itemTotal;
        private BigDecimal totalWeight;
    }

    ItemMetrics calculateMetricsAndValidateStock(List<OrderItemReq> items);
    void processAndSaveOrderItems(String orderId, List<OrderItemReq> items);
}

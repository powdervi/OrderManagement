package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.common.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RecentOrderRes {
    private String orderId;
    private String orderCode;
    private String customerName;
    private LocalDateTime createdAt;
    private BigDecimal amount;
    private OrderStatus status;
    private String assignedCarrier;
}
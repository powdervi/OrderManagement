package com.example.ordermanagement.common;

public enum OrderStatus {
    PENDING,
    AWAITING_PAYMENT,
    CONFIRMED,
    PICKING,
    SHIPPING,
    DELIVERED,
    FAILED,
    REATTEMPT,
    RETURNING,
    RETURNED,
    CANCELLED
}

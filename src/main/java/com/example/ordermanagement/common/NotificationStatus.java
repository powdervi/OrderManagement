package com.example.ordermanagement.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationStatus {
    PENDING,
    PROCESSING,
    SENT,
    FAILED,
    CANCELLED
}

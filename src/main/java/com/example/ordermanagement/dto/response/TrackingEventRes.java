package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.common.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TrackingEventRes {
    private OrderStatus status;
    private String note;
    private String location;
    private String changedBy;
    private LocalDateTime changedAt;
}

package com.example.ordermanagement.dto.request;

import com.example.ordermanagement.common.OrderStatus;
import lombok.Data;

@Data
public class RecentOrderSearchReq {
    private String keyword;
    private OrderStatus status;
    private String carrierId;
}
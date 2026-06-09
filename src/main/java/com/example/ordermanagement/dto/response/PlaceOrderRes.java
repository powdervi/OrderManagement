package com.example.ordermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOrderRes {
    private String orderId;
    private String orderCode;
    private String trackingNumber;
}

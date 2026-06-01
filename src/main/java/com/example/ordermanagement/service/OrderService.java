package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.OrderSummaryReq;
import com.example.ordermanagement.dto.request.PlaceOrderReq;
import com.example.ordermanagement.dto.response.OrderSummaryRes;
import com.example.ordermanagement.dto.response.PlaceOrderRes;

public interface OrderService {

    OrderSummaryRes getSummary(OrderSummaryReq req);

    PlaceOrderRes placeOrder(PlaceOrderReq req);
}

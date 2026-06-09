package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.OrderSummaryReq;
import com.example.ordermanagement.dto.request.PlaceOrderReq;
import com.example.ordermanagement.dto.request.UpdateOrderStatusReq;
import com.example.ordermanagement.dto.response.OrderDetailRes;
import com.example.ordermanagement.dto.response.OrderSummaryRes;
import com.example.ordermanagement.dto.response.PlaceOrderRes;
import com.example.ordermanagement.dto.response.TrackingTimelineRes;

public interface OrderService {

    OrderSummaryRes getSummary(OrderSummaryReq req);

    PlaceOrderRes placeOrder(PlaceOrderReq req);

    void updateStatus(String orderId, UpdateOrderStatusReq req);

    OrderDetailRes getOrderDetail(String orderId);

    TrackingTimelineRes getTrackingTimeline(String orderId);
}

package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.response.OrderIssueRes;

import java.math.BigDecimal;
import java.util.List;

import com.example.ordermanagement.dto.request.OrderItemReq;


public interface PricingService {
    BigDecimal calculateSubtotalForSummary(List<OrderItemReq> items, List<OrderIssueRes> issues);
}

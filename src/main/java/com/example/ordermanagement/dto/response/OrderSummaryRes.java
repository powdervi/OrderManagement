package com.example.ordermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderSummaryRes {

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal shippingFee;

    private BigDecimal grandTotal;

    private Boolean valid;

    private List<OrderIssueRes> issues;

    private CarrierRes carrier;
}

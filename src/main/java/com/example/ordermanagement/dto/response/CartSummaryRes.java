package com.example.ordermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartSummaryRes {

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal shippingFee;

    private BigDecimal grandTotal;

    private CarrierRes carrier;

    private Boolean valid;

    private List<CartIssueRes> issues;
}

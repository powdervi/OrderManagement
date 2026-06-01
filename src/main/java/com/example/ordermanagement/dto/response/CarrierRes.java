package com.example.ordermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CarrierRes {

    private String id;

    private String name;

    private String phone;

    private BigDecimal shippingFee;
}

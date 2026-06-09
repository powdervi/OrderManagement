package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.common.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInfoRes {
    private String paymentMethodName;
    private PaymentStatus paymentStatus;
}

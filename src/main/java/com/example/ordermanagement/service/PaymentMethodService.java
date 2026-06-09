package com.example.ordermanagement.service;

import com.example.ordermanagement.entity.PaymentMethod;

public interface PaymentMethodService {
    PaymentMethod validatePaymentMethod(String paymentMethodId);

}

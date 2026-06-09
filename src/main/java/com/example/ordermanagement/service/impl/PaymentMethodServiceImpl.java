package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.PaymentMethodStatus;
import com.example.ordermanagement.entity.PaymentMethod;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.PaymentMethodRepo;
import com.example.ordermanagement.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepo  paymentMethodRepo;

    @Override
    public PaymentMethod validatePaymentMethod(String paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodRepo.findById(paymentMethodId).orElseThrow(() -> new MHException(MHErrors.PAYMENT_METHOD_NOT_FOUND));
        if (paymentMethod.getStatus() != PaymentMethodStatus.ACTIVE) {
            throw new MHException(MHErrors.INVALID_PAYMENT_METHOD);
        }
        return paymentMethod;
    }
}

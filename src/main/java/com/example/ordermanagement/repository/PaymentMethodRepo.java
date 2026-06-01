package com.example.ordermanagement.repository;

import com.example.ordermanagement.common.PaymentMethodStatus;
import com.example.ordermanagement.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentMethodRepo extends JpaRepository<PaymentMethod, String> {

    List<PaymentMethod> findAllByStatus(PaymentMethodStatus status);

}

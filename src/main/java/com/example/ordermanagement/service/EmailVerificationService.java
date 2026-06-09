package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.RegisterSendOtpRequest;
import com.example.ordermanagement.entity.EmailVerification;

public interface EmailVerificationService {
    EmailVerification createVerification(RegisterSendOtpRequest request);
}

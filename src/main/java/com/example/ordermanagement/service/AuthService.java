package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.RegisterSendOtpRequest;
import com.example.ordermanagement.dto.request.RegisterVerifyOtpRequest;

public interface AuthService {
    void sendRegisterOtp(RegisterSendOtpRequest request);
    void verifyRegisterOtp(RegisterVerifyOtpRequest request);
}

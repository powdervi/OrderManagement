package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.RegisterSendOtpRequest;
import com.example.ordermanagement.dto.request.RegisterVerifyOtpRequest;
import com.example.ordermanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/send-otp")
    public ResponseEntity<BaseResponse<String>> sendRegisterOtp(
            @Valid @RequestBody RegisterSendOtpRequest request
    ) {
        authService.sendRegisterOtp(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess("OTP sent successfully"));
    }

    @PostMapping("/register/verify")
    public ResponseEntity<BaseResponse<String>> verifyRegisterOtp(
            @Valid @RequestBody RegisterVerifyOtpRequest request
    ) {
        authService.verifyRegisterOtp(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess("Register successfully"));
    }
}

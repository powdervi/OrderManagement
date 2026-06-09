package com.example.ordermanagement.service;

public interface EmailService {
    void sendOtpEmail(String to, String otp);

    void sendEmail(String to, String subject, String content);
}

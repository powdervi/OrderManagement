package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.OtpStatus;
import com.example.ordermanagement.common.UserRole;
import com.example.ordermanagement.common.UserStatus;
import com.example.ordermanagement.dto.request.RegisterSendOtpRequest;
import com.example.ordermanagement.dto.request.RegisterVerifyOtpRequest;
import com.example.ordermanagement.entity.EmailVerification;
import com.example.ordermanagement.entity.User;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.EmailVerificationRepository;
import com.example.ordermanagement.repository.UserRepo;
import com.example.ordermanagement.service.AuthService;
import com.example.ordermanagement.service.EmailService;
import com.example.ordermanagement.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepository;
    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    public void sendRegisterOtp(
            RegisterSendOtpRequest request
    ) {
        EmailVerification verification = emailVerificationService
                .createVerification(request);
        try {
            emailService.sendOtpEmail(
                    verification.getEmail(),
                    verification.getOtpCode()
            );
            verification.setStatus(OtpStatus.SENT);
            verification.setSentAt(LocalDateTime.now());
            verification.setLastError(null);
        } catch (Exception ex) {
            verification.setStatus(OtpStatus.FAILED);
            verification.setLastError(ex.getMessage());
        }
        emailVerificationRepository.save(verification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verifyRegisterOtp(RegisterVerifyOtpRequest request) {

        EmailVerification verification = emailVerificationRepository
                .findTopByEmailOrderByCreatedAtDesc(request.getEmail())
                .orElseThrow(() -> new MHException(MHErrors.OTP_NOT_FOUND));

        if (verification.getStatus() == OtpStatus.VERIFIED) {
            throw new MHException(MHErrors.OTP_ALREADY_USED);
        }

        if (verification.getStatus() != OtpStatus.SENT) {
            throw new MHException(MHErrors.OTP_NOT_FOUND);
        }

        if (verification.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new MHException(MHErrors.OTP_EXPIRED);
        }

        if (!verification.getOtpCode().equals(request.getOtp())) {
            throw new MHException(MHErrors.INVALID_OTP);
        }

        if (userRepository.existsByUsername(verification.getUsername())) {
            throw new MHException(MHErrors.USER_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(verification.getEmail())) {
            throw new MHException(MHErrors.EMAIL_ALREADY_EXITS);
        }

        if (userRepository.existsByPhone(verification.getPhone())) {
            throw new MHException(MHErrors.PHONE_ALREADY_EXITS);
        }

        User user = new User();

        user.setUsername(verification.getUsername());
        user.setFirstName(verification.getFirstName());
        user.setLastName(verification.getLastName());
        user.setEmail(verification.getEmail());
        user.setPhone(verification.getPhone());
        user.setPasswordHash(verification.getPasswordHash());
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        verification.setStatus(OtpStatus.VERIFIED);
        emailVerificationRepository.save(verification);
    }
}

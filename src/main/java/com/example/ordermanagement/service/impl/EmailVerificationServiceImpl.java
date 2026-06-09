package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.OtpStatus;
import com.example.ordermanagement.dto.request.RegisterSendOtpRequest;
import com.example.ordermanagement.entity.EmailVerification;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.EmailVerificationRepository;
import com.example.ordermanagement.repository.UserRepo;
import com.example.ordermanagement.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final UserRepo userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmailVerification  createVerification(RegisterSendOtpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new MHException(MHErrors.USER_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new MHException(MHErrors.EMAIL_ALREADY_EXITS);
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new MHException(MHErrors.PHONE_ALREADY_EXITS);
        }
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        emailVerificationRepository.deleteByEmail(request.getEmail());
        EmailVerification verification = EmailVerification.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role("CUSTOMER")
                .otpCode(otp)
                .status(OtpStatus.PENDING)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .createdAt(LocalDateTime.now())
                .build();
        emailVerificationRepository.save(verification);
        return verification;
    }
}

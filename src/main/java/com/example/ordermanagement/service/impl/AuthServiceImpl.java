package com.example.ordermanagement.service.impl;

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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void sendRegisterOtp(RegisterSendOtpRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {

            throw new MHException(MHErrors.USER_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new MHException(MHErrors.EMAIL_ALREADY_EXITS);
        }

        if (userRepository.existsByPhone(request.getPhone())) {

            throw new MHException(MHErrors.PHONE_ALREADY_EXITS);
        }

        String otp = generateOtp();

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

                .expiredAt(LocalDateTime.now().plusMinutes(5))

                .createdAt(LocalDateTime.now())

                .build();

        emailVerificationRepository.save(verification);

        emailService.sendOtpEmail(request.getEmail(), otp);
    }

    @Override
    @Transactional
    public void verifyRegisterOtp(RegisterVerifyOtpRequest request) {

        EmailVerification verification =

                emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(request.getEmail()).orElseThrow(() -> new MHException(MHErrors.OTP_NOT_FOUND));

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

        emailVerificationRepository.delete(verification);
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}

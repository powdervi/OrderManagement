package com.example.ordermanagement.service.schedule;

import com.example.ordermanagement.common.OtpStatus;
import com.example.ordermanagement.entity.EmailVerification;
import com.example.ordermanagement.repository.EmailVerificationRepository;
import com.example.ordermanagement.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OtpRetryScheduler {

    private static final int BATCH_SIZE = 100;

    private final EmailVerificationRepository repository;

    private final EmailService emailService;

    @Scheduled(fixedDelay = 30000)
    public void retryFailedOtp() {
        long totalFailed = repository.countByStatus(OtpStatus.FAILED);
        if (totalFailed == 0) {
            return;
        }
        int totalLoop = (int) Math.ceil((double) totalFailed / BATCH_SIZE);
        for (int i = 0; i < totalLoop; i++) {
            List<EmailVerification> batch = repository.findTop100ByStatusOrderByCreatedAtAsc(OtpStatus.FAILED);
            if (batch.isEmpty()) {
                break;
            }
            for (EmailVerification verification : batch) {
                if (verification.getExpiredAt().isBefore(LocalDateTime.now())) {
                    continue;
                }
                try {
                    emailService.sendOtpEmail(verification.getEmail(), verification.getOtpCode());
                    verification.setStatus(OtpStatus.SENT);
                    verification.setSentAt(LocalDateTime.now());
                    verification.setLastError(null);
                } catch (Exception ex) {
                    verification.setStatus(OtpStatus.FAILED);
                    verification.setLastError(ex.getMessage());
                    log.error("Retry OTP failed for email {}", verification.getEmail(), ex);
                }
            }
            repository.saveAll(batch);
        }
    }
}
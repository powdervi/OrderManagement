package com.example.ordermanagement.repository;


import com.example.ordermanagement.common.OtpStatus;
import com.example.ordermanagement.entity.EmailVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);
    void deleteByEmail(String email);
    long countByStatus(OtpStatus status);
    Page<EmailVerification> findByStatus(OtpStatus status, Pageable pageable);
    List<EmailVerification> findTop100ByStatusOrderByCreatedAtAsc(OtpStatus status);
}

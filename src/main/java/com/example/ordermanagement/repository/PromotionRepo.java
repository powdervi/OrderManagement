package com.example.ordermanagement.repository;

import com.example.ordermanagement.common.PromotionStatus;
import com.example.ordermanagement.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PromotionRepo extends JpaRepository<Promotion, String>, JpaSpecificationExecutor<Promotion> {
    Page<Promotion> findByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(PromotionStatus status, LocalDateTime now1, LocalDateTime now2, Pageable pageable);
}

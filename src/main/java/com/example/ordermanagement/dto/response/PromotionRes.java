package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.common.DiscountType;
import com.example.ordermanagement.common.PromotionStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PromotionRes {
    private String id;
    private String code;
    private String name;
    private String description;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private PromotionStatus status;
}

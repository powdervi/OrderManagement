package com.example.ordermanagement.dto.request;

import com.example.ordermanagement.common.DiscountType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PromotionSearchRequest {

    private String code;

    private DiscountType discountType;

}

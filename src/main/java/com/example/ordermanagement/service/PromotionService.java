package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.PromotionSearchRequest;
import com.example.ordermanagement.dto.response.OrderIssueRes;
import com.example.ordermanagement.dto.response.PromotionRes;
import com.example.ordermanagement.entity.Promotion;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionService {

    Page<PromotionRes> search(Integer pageSize, Integer pageNumber, String sort, PromotionSearchRequest req);

    Page<PromotionRes> getAvailablePromotions(Integer pageSize, Integer pageNumber);

    Promotion validatePromotion(String promotionId);
    BigDecimal calculateDiscount(BigDecimal subtotal, Promotion promotion, List<OrderIssueRes> issues);
}

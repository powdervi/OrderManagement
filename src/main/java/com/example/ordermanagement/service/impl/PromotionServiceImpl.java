package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.DiscountType;
import com.example.ordermanagement.common.PromotionStatus;
import com.example.ordermanagement.dto.request.PromotionSearchRequest;
import com.example.ordermanagement.dto.response.OrderIssueRes;
import com.example.ordermanagement.dto.response.PromotionRes;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.mapper.PromotionMapper;
import com.example.ordermanagement.service.spec.PromotionSpecification;
import com.example.ordermanagement.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import com.example.ordermanagement.entity.Promotion;
import com.example.ordermanagement.repository.PromotionRepo;
import com.example.ordermanagement.service.PromotionService;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepo promotionRepo;
    private final PromotionMapper promotionMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionRes> search(Integer pageSize, Integer pageNumber, String sort, PromotionSearchRequest req) {
        Specification<Promotion> specification = Specification.unrestricted();

        if (req.getCode() != null && !req.getCode().isBlank()) {
            specification = specification.and(PromotionSpecification.likeCode(req.getCode()));
        }

        if (req.getDiscountType() != null) {
            specification = specification.and(PromotionSpecification.equalDiscountType(req.getDiscountType()));
        }
        Sort sortObj = SortUtil.buildSort(sort);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortObj);

        Page<Promotion> promotions= promotionRepo.findAll(specification, pageable);
        return promotions.map(promotionMapper::toPromotionRes);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionRes> getAvailablePromotions(Integer pageSize, Integer pageNumber) {
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Promotion> promotions = promotionRepo.findByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(PromotionStatus.ACTIVE, now, now, pageable);
        return promotions.map(promotionMapper::toPromotionRes);
    }

    @Override
    public Promotion validatePromotion(String promotionId) {
        if (promotionId == null || promotionId.isBlank()) return null;
        Promotion promotion = promotionRepo.findById(promotionId)
                .orElseThrow(() -> new MHException(MHErrors.PROMOTION_NOT_FOUND));

        if (LocalDateTime.now().isBefore(promotion.getStartAt()) || LocalDateTime.now().isAfter(promotion.getEndAt())) {
            throw new MHException(MHErrors.INVALID_PROMOTION);
        }
        return promotion;
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal, Promotion promotion, List<OrderIssueRes> issues) {
        if (promotion == null) return BigDecimal.ZERO;

        if (LocalDateTime.now().isBefore(promotion.getStartAt()) || LocalDateTime.now().isAfter(promotion.getEndAt())) {
            if (issues != null) {
                OrderIssueRes issue = new OrderIssueRes();
                issue.setType("INVALID_PROMOTION");
                issue.setMessage("Promotion expired");
                issues.add(issue);
            }
            return BigDecimal.ZERO;
        }

        BigDecimal discount = (promotion.getDiscountType() == DiscountType.PERCENT)
                ? subtotal.multiply(promotion.getDiscountValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                : promotion.getDiscountValue();

        return discount.compareTo(subtotal) > 0 ? subtotal : discount;
    }
}

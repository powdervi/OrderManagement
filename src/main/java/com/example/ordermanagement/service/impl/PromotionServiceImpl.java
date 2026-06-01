package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.PromotionStatus;
import com.example.ordermanagement.dto.request.PromotionSearchRequest;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.service.spec.ProductSpecification;
import com.example.ordermanagement.service.spec.PromotionSpecification;
import com.example.ordermanagement.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import com.example.ordermanagement.entity.Promotion;
import com.example.ordermanagement.repository.PromotionRepo;
import com.example.ordermanagement.service.PromotionService;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepo promotionRepo;

    @Override
    public Page<Promotion> search(Integer pageSize, Integer pageNumber, String sort, PromotionSearchRequest req) {
        Specification<Promotion> specification = Specification.unrestricted();

        if (req.getCode() != null && !req.getCode().isBlank()) {
            specification = specification.and(PromotionSpecification.likeCode(req.getCode()));
        }

        if (req.getDiscountType() != null) {
            specification = specification.and(PromotionSpecification.equalDiscountType(req.getDiscountType()));
        }
        Sort sortObj = SortUtil.buildSort(sort);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortObj);

        return promotionRepo.findAll(specification, pageable);
    }

    @Override
    public List<Promotion> getAvailablePromotions() {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepo.findByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(PromotionStatus.ACTIVE, now, now);
    }
}

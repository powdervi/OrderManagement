package com.example.ordermanagement.service.spec;

import com.example.ordermanagement.common.DiscountType;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.entity.Promotion;
import org.springframework.data.jpa.domain.Specification;

public class PromotionSpecification {

    public static Specification<Promotion> likeCode(String code) {
        return (root, query, criteriaBuilder) -> {
            if (code == null || code.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("code")),
                    "%" + code.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Promotion> equalDiscountType(DiscountType discountType) {
        return (root, query, criteriaBuilder) -> {
            if (discountType == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("discountType"), discountType);
        };
    }
}

package com.example.ordermanagement.service.spec;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.entity.Order;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    public static Specification<Order> likeKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            String searchPattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("orderCode")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("recipientNameSnapshot")), searchPattern)
            );
        };
    }

    public static Specification<Order> equalStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("orderStatus"), status);
        };
    }

    public static Specification<Order> equalCarrierId(String carrierId) {
        return (root, query, criteriaBuilder) -> {
            if (carrierId == null || carrierId.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("carrierId"), carrierId);
        };
    }
}
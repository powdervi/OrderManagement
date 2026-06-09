package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.common.PaymentMethodStatus;
import com.example.ordermanagement.common.UserRole;
import com.example.ordermanagement.dto.request.OrderItemReq;
import com.example.ordermanagement.entity.Address;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.entity.PaymentMethod;
import com.example.ordermanagement.entity.User;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.AddressRepo;
import com.example.ordermanagement.repository.PaymentMethodRepo;
import com.example.ordermanagement.repository.UserRepo;
import com.example.ordermanagement.service.OrderValidationService;
import com.example.ordermanagement.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderValidationServiceImpl implements OrderValidationService {
    private final UserRepo userRepo;

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.PENDING, Set.of(OrderStatus.CONFIRMED),
            OrderStatus.CONFIRMED, Set.of(OrderStatus.PICKING),
            OrderStatus.PICKING, Set.of(OrderStatus.SHIPPING),
            OrderStatus.SHIPPING, Set.of(OrderStatus.DELIVERED, OrderStatus.FAILED),
            OrderStatus.FAILED, Set.of(OrderStatus.REATTEMPT, OrderStatus.RETURNING),
            OrderStatus.REATTEMPT, Set.of(OrderStatus.SHIPPING),
            OrderStatus.RETURNING, Set.of(OrderStatus.RETURNED));

    @Override
    public void validateDuplicateProducts(List<OrderItemReq> items) {
        Set<String> productIds = new HashSet<>();
        for (OrderItemReq item : items) {
            if (!productIds.add(item.getProductId())) {
                throw new MHException(MHErrors.DUPLICATE_PRODUCT);
            }
        }
    }

    @Override
    public void validateOrderAccess(Order order) {
        String currentUserId = SecurityUtils.getCurrentUserId();
        User currentUser = userRepo.findById(currentUserId).orElseThrow(() -> new MHException(MHErrors.USER_NOT_FOUND));

        if (currentUser.getRole() == UserRole.CUSTOMER && !order.getUserId().equals(currentUserId)) {
            throw new MHException(MHErrors.ACCESS_DENIED);
        }
    }

    @Override
    public void validateTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        Set<OrderStatus> allowedStatuses = ALLOWED_TRANSITIONS.get(currentStatus);
        if (allowedStatuses == null || !allowedStatuses.contains(newStatus)) {
            throw new MHException(MHErrors.INVALID_ORDER_STATUS_TRANSITION);
        }
    }

    @Override
    public void validateRolePermission(String userId, OrderStatus currentStatus, OrderStatus newStatus) {
        User user = userRepo.findById(userId).orElseThrow(() -> new MHException(MHErrors.USER_NOT_FOUND));
        UserRole role = user.getRole();
        if (role == UserRole.ADMIN) return;
        if (role == UserRole.WAREHOUSE_STAFF) {
            boolean valid = (currentStatus == OrderStatus.PENDING && newStatus == OrderStatus.CONFIRMED) || (currentStatus == OrderStatus.CONFIRMED && newStatus == OrderStatus.PICKING);
            if (!valid) {
                throw new MHException(MHErrors.ROLE_NOT_ALLOWED_TO_UPDATE_ORDER_STATUS);
            }
            return;
        }
        if (role == UserRole.SHIPPER) {
            boolean valid = (currentStatus == OrderStatus.PICKING && newStatus == OrderStatus.SHIPPING) || (currentStatus == OrderStatus.SHIPPING && newStatus == OrderStatus.DELIVERED) || (currentStatus == OrderStatus.SHIPPING && newStatus == OrderStatus.FAILED);
            if (!valid) {
                throw new MHException(MHErrors.ROLE_NOT_ALLOWED_TO_UPDATE_ORDER_STATUS);
            }
            return;
        }

        throw new MHException(MHErrors.ROLE_NOT_ALLOWED_TO_UPDATE_ORDER_STATUS);
    }
}

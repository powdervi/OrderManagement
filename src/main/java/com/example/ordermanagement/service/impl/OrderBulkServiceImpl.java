package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.dto.request.BulkConfirmOrderReq;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.OrderRepo;
import com.example.ordermanagement.service.NotificationService;
import com.example.ordermanagement.service.OrderBulkService;
import com.example.ordermanagement.service.OrderValidationService;
import com.example.ordermanagement.service.TrackingLogService;
import com.example.ordermanagement.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderBulkServiceImpl implements OrderBulkService {

    private final OrderRepo orderRepo;
    private final OrderValidationService validationService;
    private final TrackingLogService trackingLogService;
    private final NotificationService notificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bulkConfirmOrders(BulkConfirmOrderReq req) {
        String userId = SecurityUtils.getCurrentUserId();

        List<Order> orders = orderRepo.findAllById(req.getOrderIds());

        if (orders.isEmpty() || orders.size() != req.getOrderIds().size()) {
            throw new MHException(MHErrors.ORDER_NOT_FOUND);
        }

        for (Order order : orders) {
            OrderStatus currentStatus = order.getOrderStatus();
            OrderStatus newStatus = OrderStatus.CONFIRMED;

            validationService.validateTransition(currentStatus, newStatus);
            validationService.validateRolePermission(userId, currentStatus, newStatus);

            order.setOrderStatus(newStatus);
        }

        orderRepo.saveAll(orders);

        for (Order order : orders) {
            trackingLogService.logTracking(order.getId(), OrderStatus.CONFIRMED,
                    "Order bulk confirmed by system", null, userId);
            notificationService.createNotification(order, OrderStatus.CONFIRMED);
        }

        log.info("Successfully bulk confirmed {} orders by user {}", orders.size(), userId);
    }
}
package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.NotificationStatus;
import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.entity.Notification;
import com.example.ordermanagement.entity.NotificationTemplate;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.NotificationRepository;
import com.example.ordermanagement.repository.NotificationTemplateRepository;
import com.example.ordermanagement.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationTemplateRepository notificationTemplateRepo;
    private final NotificationRepository notificationRepo;

    @Override
    @Transactional
    public void createNotification(Order order, OrderStatus status) {
        String templateCode = getTemplateCode(status);
        NotificationTemplate template = notificationTemplateRepo.findByTemplateCodeAndIsActiveTrue(templateCode)
                .orElseThrow(() -> new MHException(MHErrors.NOTIFICATION_TEMPLATE_NOT_FOUND));

        String title = template.getTitleTemplate();
        String content = replacePlaceholders(template.getContentTemplate(), order);

        Notification notification = Notification.builder()
                .userId(order.getUserId())
                .orderId(order.getId())
                .notificationTemplateId(template.getId())
                .channel(template.getChannel())
                .title(title)
                .content(content)
                .status(NotificationStatus.PENDING)
                .scheduledAt(LocalDateTime.now())
                .build();

        notificationRepo.save(notification);
    }

    private String replacePlaceholders(String template, Order order) {
        return template.replace("{orderCode}", order.getOrderCode())
                .replace("{trackingNumber}", Optional.ofNullable(order.getTrackingNumber()).orElse(""))
                .replace("{carrierName}", Optional.ofNullable(order.getCarrierNameSnapshot()).orElse(""));
    }

    private String getTemplateCode(OrderStatus status) {
        return switch (status) {
            case PENDING -> "ORDER_PENDING";
            case CONFIRMED -> "ORDER_CONFIRMED";
            case PICKING -> "ORDER_PICKING";
            case SHIPPING -> "ORDER_SHIPPING";
            case DELIVERED -> "ORDER_DELIVERED";
            case FAILED -> "ORDER_FAILED";
            case RETURNING -> "ORDER_RETURNING";
            case RETURNED -> "ORDER_RETURNED";
            default -> throw new MHException(MHErrors.NOTIFICATION_TEMPLATE_NOT_FOUND);
        };
    }
}

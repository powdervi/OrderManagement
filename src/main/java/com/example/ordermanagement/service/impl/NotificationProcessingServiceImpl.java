package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.NotificationStatus;
import com.example.ordermanagement.entity.Notification;
import com.example.ordermanagement.repository.NotificationRepository;
import com.example.ordermanagement.service.NotificationProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationProcessingServiceImpl implements NotificationProcessingService {

    private final NotificationRepository notificationRepo;

    @Transactional(rollbackFor = Exception.class)
    public List<Notification> claimPendingNotifications(int limit) {

        Pageable pageable = PageRequest.of(0, limit);

        List<Notification> notifications = notificationRepo.findByStatusAndScheduledAtBeforeOrderByCreatedAtAsc(
                NotificationStatus.PENDING,
                LocalDateTime.now(),
                pageable
        );

        if (notifications.isEmpty()) {
            return notifications;
        }

        notifications.forEach(n -> n.setStatus(NotificationStatus.PROCESSING));
        notificationRepo.saveAll(notifications);

        return notifications;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBatchProcessedNotifications(List<Notification> sentNotifications) {
        notificationRepo.saveAll(sentNotifications);
    }
}

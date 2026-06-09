package com.example.ordermanagement.schedule;

import com.example.ordermanagement.common.NotificationStatus;
import com.example.ordermanagement.entity.Notification;
import com.example.ordermanagement.entity.User;
import com.example.ordermanagement.repository.UserRepo;
import com.example.ordermanagement.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.ordermanagement.service.NotificationProcessingService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRetryScheduler {

    private final NotificationProcessingService processingService;
    private final UserRepo userRepo;
    private final EmailService emailService;

    @Scheduled(fixedDelay = 30000)
    public void processPendingNotifications() {
        int pageSize = 50;

        while (true) {
            List<Notification> notifications = processingService.claimPendingNotifications(pageSize);

            if (notifications.isEmpty()) {
                break;
            }

            Set<String> userIds = notifications.stream().map(Notification::getUserId).collect(Collectors.toSet());
            Map<String, User> userMap = userRepo.findAllById(userIds).stream()
                    .collect(Collectors.toMap(User::getId, Function.identity()));

            for (Notification notification : notifications) {
                User user = userMap.get(notification.getUserId());
                if (user != null) {
                    try {
                        emailService.sendEmail(user.getEmail(), notification.getTitle(), notification.getContent());

                        notification.setStatus(NotificationStatus.SENT);
                        notification.setSentAt(LocalDateTime.now());
                    } catch (Exception ex) {
                        notification.setStatus(NotificationStatus.FAILED);
                        log.error("Failed to send notification {}", notification.getId(), ex);
                    }
                } else {
                    notification.setStatus(NotificationStatus.FAILED);
                    log.warn("User not found for notification ID: {}", notification.getId());
                }
            }

            processingService.saveBatchProcessedNotifications(notifications);
        }
    }
}
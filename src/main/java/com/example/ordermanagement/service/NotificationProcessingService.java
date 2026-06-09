package com.example.ordermanagement.service;

import com.example.ordermanagement.entity.Notification;

import java.util.List;

public interface NotificationProcessingService {
    List<Notification> claimPendingNotifications(int limit);
    void saveBatchProcessedNotifications(List<Notification> sentNotifications);
}

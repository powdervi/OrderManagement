package com.example.ordermanagement.entity;

import com.example.ordermanagement.common.NotificationChannel;
import com.example.ordermanagement.common.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends AbstractEntity {

    private String userId;

    private String orderId;

    private String notificationTemplateId;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime scheduledAt;

    private LocalDateTime sentAt;
}

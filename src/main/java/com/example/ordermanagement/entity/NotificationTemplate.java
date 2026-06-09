package com.example.ordermanagement.entity;

import com.example.ordermanagement.common.NotificationChannel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTemplate extends AbstractEntity {

    private String titleTemplate;

    @Column(name = "template_code")
    private String templateCode;

    @Column(columnDefinition = "TEXT")
    private String contentTemplate;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    private Boolean isActive;
}

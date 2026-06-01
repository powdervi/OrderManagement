package com.example.ordermanagement.entity;

import com.example.ordermanagement.common.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tracking_logs")
public class TrackingLog extends AbstractEntity {

    @Column(name = "order_id")
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "note")
    private String note;

    @Column(name = "location")
    private String location;

    @Column(name = "changed_by_user_id")
    private String changedByUserId;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}

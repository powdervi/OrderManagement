package com.example.ordermanagement.repository;

import com.example.ordermanagement.common.NotificationStatus;
import com.example.ordermanagement.entity.Notification;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByStatusOrderByCreatedAtAsc(NotificationStatus status);

    List<Notification> findTop50ByStatusAndScheduledAtBeforeOrderByCreatedAtAsc(NotificationStatus notificationStatus, LocalDateTime now);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Notification> findByStatusAndScheduledAtBeforeOrderByCreatedAtAsc(NotificationStatus notificationStatus,
                                                                           LocalDateTime now, Pageable pageable);
}

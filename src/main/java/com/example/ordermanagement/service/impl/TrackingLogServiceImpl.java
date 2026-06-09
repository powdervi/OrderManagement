package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.entity.TrackingLog;
import com.example.ordermanagement.repository.TrackingLogRepo;
import com.example.ordermanagement.service.TrackingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackingLogServiceImpl implements TrackingLogService {

    private final TrackingLogRepo trackingLogRepo;

    @Override
    @Transactional(readOnly = true)
    public List<TrackingLog> getTrackingLogs(String orderId) {
        return trackingLogRepo.findByOrderIdOrderByChangedAtAsc(orderId);
    }

    @Override
    @Transactional
    public void logTracking(String orderId, OrderStatus status, String note, String location, String userId) {
        TrackingLog trackingLog = new TrackingLog();
        trackingLog.setOrderId(orderId);
        trackingLog.setStatus(status);
        trackingLog.setNote(note);
        trackingLog.setLocation(location);
        trackingLog.setChangedByUserId(userId);
        trackingLog.setChangedAt(LocalDateTime.now());

        trackingLogRepo.save(trackingLog);
    }
}

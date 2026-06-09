package com.example.ordermanagement.service;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.entity.TrackingLog;

import java.util.List;

public interface TrackingLogService {
    List<TrackingLog> getTrackingLogs(String orderId);

    void logTracking(String orderId, OrderStatus status, String note, String location, String userId);
}

package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.TrackingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackingLogRepo extends JpaRepository<TrackingLog, String> {

    List<TrackingLog> findByOrderIdOrderByChangedAtAsc(String orderId);
}

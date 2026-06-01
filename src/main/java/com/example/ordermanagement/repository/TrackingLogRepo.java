package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.TrackingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingLogRepo extends JpaRepository<TrackingLog, String> {
}

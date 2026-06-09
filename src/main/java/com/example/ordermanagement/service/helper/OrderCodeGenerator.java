package com.example.ordermanagement.service.helper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderCodeGenerator {

    private static final long CUSTOM_EPOCH = 1704067200000L;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    private synchronized long generateUniqueId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Lỗi hệ thống: Đồng hồ bị lùi thời gian!");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & 4095;

            if (sequence == 0) {
                while (currentTimestamp <= lastTimestamp) {
                    currentTimestamp = System.currentTimeMillis();
                }
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - CUSTOM_EPOCH) << 12) | sequence;
    }

    public String generateOrderCode() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String uniquePart = Long.toString(generateUniqueId(), 36).toUpperCase();
        return "ORD-" + datePart + "-" + uniquePart;
    }

    public String generateTrackingNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String uniquePart = Long.toString(generateUniqueId(), 36).toUpperCase();
        return "TRK-" + datePart + "-" + uniquePart;
    }

    public String generateProductSku() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMM"));
        String uniquePart = Long.toString(generateUniqueId(), 36).toUpperCase();
        return "PRD-" + datePart + "-" + uniquePart;
    }
}
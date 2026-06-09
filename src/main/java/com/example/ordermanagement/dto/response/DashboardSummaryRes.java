package com.example.ordermanagement.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DashboardSummaryRes {
    private RevenueMetrics totalRevenue;
    private long totalOrders;
    private OrderStatusMetrics orderStatuses;

    @Data
    @Builder
    public static class RevenueMetrics {
        private BigDecimal value;
        private double growthPercentage;
        private String trend;
    }

    @Data
    @Builder
    public static class OrderStatusMetrics {
        private long pending;
        private long shipping;
        private FailedMetrics failed;

        @Data
        @Builder
        public static class FailedMetrics {
            private long count;
            private double errorRate;
        }
    }
}
package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.dto.request.BulkConfirmOrderReq;
import com.example.ordermanagement.dto.request.RecentOrderSearchReq;
import com.example.ordermanagement.dto.response.DashboardSummaryRes;
import com.example.ordermanagement.dto.response.RecentOrderRes;
import com.example.ordermanagement.entity.Order;
import com.example.ordermanagement.mapper.OrderMapper;
import com.example.ordermanagement.repository.OrderRepo;
import com.example.ordermanagement.service.DashboardService;
import com.example.ordermanagement.service.OrderBulkService;
import com.example.ordermanagement.service.spec.OrderSpecification;
import com.example.ordermanagement.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;
    private final OrderBulkService orderBulkService;


    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryRes getSummaryMetrics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfThisMonth = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime startOfLastMonth = YearMonth.now().minusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime endOfLastMonth = YearMonth.now().minusMonths(1).atEndOfMonth().atTime(23, 59, 59);

        long totalOrdersThisMonth = orderRepo.countTotalOrders(startOfThisMonth, now);
        BigDecimal revenueThisMonth = orderRepo.calculateTotalRevenue(startOfThisMonth, now, OrderStatus.DELIVERED);
        BigDecimal revenueLastMonth = orderRepo.calculateTotalRevenue(startOfLastMonth, endOfLastMonth, OrderStatus.DELIVERED);
        List<Object[]> rawStatusCounts = orderRepo.countOrdersGroupedByStatus(startOfThisMonth, now);

        double growthPercentage = calculateGrowthPercentage(revenueThisMonth, revenueLastMonth);
        String trend = growthPercentage >= 0 ? "UP" : "DOWN";

        Map<String, Long> statusMap = parseStatusCounts(rawStatusCounts);
        long pendingCount = statusMap.getOrDefault(OrderStatus.PENDING.name(), 0L);
        long shippingCount = statusMap.getOrDefault(OrderStatus.SHIPPING.name(), 0L);
        long failedCount = statusMap.getOrDefault(OrderStatus.FAILED.name(), 0L);

        double errorRate = calculateErrorRate(failedCount, totalOrdersThisMonth);

        return buildResponse(revenueThisMonth, growthPercentage, trend,
                totalOrdersThisMonth, pendingCount, shippingCount, failedCount, errorRate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecentOrderRes> getRecentOrders(Integer pageSize, Integer pageNumber, String sort, RecentOrderSearchReq req) {
        Specification<Order> spec = Specification.unrestricted();

        if (req != null) {
            if (req.getKeyword() != null && !req.getKeyword().isBlank()) {
                spec = spec.and(OrderSpecification.likeKeyword(req.getKeyword()));
            }
            if (req.getStatus() != null) {
                spec = spec.and(OrderSpecification.equalStatus(req.getStatus()));
            }
            if (req.getCarrierId() != null && !req.getCarrierId().isBlank()) {
                spec = spec.and(OrderSpecification.equalCarrierId(req.getCarrierId()));
            }
        }

        Sort sortObj = SortUtil.buildSort(sort != null ? sort : "-createdAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortObj);

        Page<Order> orders = orderRepo.findAll(spec, pageable);

        return orders.map(orderMapper::toRecentOrderRes);
    }

    @Override
    public void bulkConfirmOrders(BulkConfirmOrderReq req) {
        orderBulkService.bulkConfirmOrders(req);
    }

    private double calculateGrowthPercentage(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) <= 0) {
            return 0.0;
        }
        BigDecimal diff = current.subtract(previous);
        double percentage = diff.divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
        return Math.round(percentage * 10.0) / 10.0;
    }

    private double calculateErrorRate(long failedCount, long totalOrders) {
        if (totalOrders <= 0) return 0.0;
        double rate = ((double) failedCount / totalOrders) * 100;
        return Math.round(rate * 10.0) / 10.0;
    }

    private Map<String, Long> parseStatusCounts(List<Object[]> statusCounts) {
        return statusCounts.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]
                ));
    }

    private DashboardSummaryRes buildResponse(BigDecimal revenue, double growth, String trend,
                                              long totalOrders, long pending, long shipping,
                                              long failed, double errorRate) {
        return DashboardSummaryRes.builder()
                .totalRevenue(DashboardSummaryRes.RevenueMetrics.builder()
                        .value(revenue)
                        .growthPercentage(Math.abs(growth))
                        .trend(trend)
                        .build())
                .totalOrders(totalOrders)
                .orderStatuses(DashboardSummaryRes.OrderStatusMetrics.builder()
                        .pending(pending)
                        .shipping(shipping)
                        .failed(DashboardSummaryRes.OrderStatusMetrics.FailedMetrics.builder()
                                .count(failed)
                                .errorRate(errorRate)
                                .build())
                        .build())
                .build();
    }
}
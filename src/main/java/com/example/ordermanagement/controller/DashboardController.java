package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.BulkConfirmOrderReq;
import com.example.ordermanagement.dto.request.RecentOrderSearchReq;
import com.example.ordermanagement.dto.response.DashboardSummaryRes;
import com.example.ordermanagement.dto.response.RecentOrderRes;
import com.example.ordermanagement.service.DashboardService;
import com.example.ordermanagement.service.OrderBulkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/summary-metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_STAFF')")
    public ResponseEntity<BaseResponse<DashboardSummaryRes>> getSummaryMetrics() {
        DashboardSummaryRes response = dashboardService.getSummaryMetrics();
        return ResponseEntity.ok(BaseResponse.ofSuccess(response));
    }

    @GetMapping("/recent-orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_STAFF')")
    public ResponseEntity<BaseResponse<List<RecentOrderRes>>> getRecentOrders(
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "page_number", defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false) String sort,
            RecentOrderSearchReq req) {

        Page<RecentOrderRes> result = dashboardService.getRecentOrders(pageSize, pageNumber, sort, req);
        return ResponseEntity.ok(BaseResponse.ofSuccess(result));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_STAFF')")
    @PutMapping("/bulk-confirm")
    public ResponseEntity<BaseResponse<Void>> bulkConfirmOrders(@RequestBody @Valid BulkConfirmOrderReq req) {
        dashboardService.bulkConfirmOrders(req);
        return ResponseEntity.ok(BaseResponse.ofSuccess("Orders have been bulk confirmed successfully"));
    }
}
package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.BulkConfirmOrderReq;
import com.example.ordermanagement.dto.request.RecentOrderSearchReq;
import com.example.ordermanagement.dto.response.DashboardSummaryRes;
import com.example.ordermanagement.dto.response.RecentOrderRes;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface DashboardService {
    DashboardSummaryRes getSummaryMetrics();
    Page<RecentOrderRes> getRecentOrders(Integer pageSize, Integer pageNumber, String sort, RecentOrderSearchReq req);

    void bulkConfirmOrders(@Valid BulkConfirmOrderReq req);
}

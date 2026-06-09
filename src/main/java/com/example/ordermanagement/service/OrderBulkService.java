package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.BulkConfirmOrderReq;

public interface OrderBulkService {
    void bulkConfirmOrders(BulkConfirmOrderReq req);
}
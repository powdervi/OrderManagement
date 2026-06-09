package com.example.ordermanagement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InventorySummaryRes {
    private BigDecimal totalInventoryValue;
    private long totalProducts;
    private long lowStockCount;
}
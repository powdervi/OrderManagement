package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.InventoryCreateReq;
import com.example.ordermanagement.dto.request.InventoryUpdateReq;
import com.example.ordermanagement.entity.Inventory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface InventoryService {
    Inventory create(String productId, @Valid InventoryCreateReq req);

    Inventory update(String id, @Valid InventoryUpdateReq req);

    void checkAvailableStock( String productId, @NotNull(message = "Quantity must not be null") @Min(value = 1, message = "Quantity must be greater than 0") Integer quantity);
}

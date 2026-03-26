package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.InventoryCreateReq;
import com.example.ordermanagement.dto.request.InventoryUpdateReq;
import com.example.ordermanagement.entity.Inventory;
import jakarta.validation.Valid;

public interface InventoryService {
    Inventory create(String productId, @Valid InventoryCreateReq req);

    Inventory update(String id, @Valid InventoryUpdateReq req);
}

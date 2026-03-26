package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.InventoryCreateReq;
import com.example.ordermanagement.dto.request.InventoryUpdateReq;
import com.example.ordermanagement.dto.response.InventoryRes;
import com.example.ordermanagement.entity.Inventory;
import com.example.ordermanagement.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InventoryController {
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @PostMapping("/products/{productId}/inventory")
    public ResponseEntity<BaseResponse<InventoryRes>> createInventory(
            @PathVariable String productId,
            @RequestBody @Valid InventoryCreateReq req
    ) {
        Inventory inventory = inventoryService.create(productId, req);
        InventoryRes res = modelMapper.map(inventory, InventoryRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(res));
    }

    @PutMapping("/inventory/{id}")
    public ResponseEntity<BaseResponse<InventoryRes>> updateInventory(
            @PathVariable String id,
            @RequestBody @Valid InventoryUpdateReq req
    ) {
        Inventory inventory = inventoryService.update(id, req);
        InventoryRes res = modelMapper.map(inventory, InventoryRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(res));
    }

}

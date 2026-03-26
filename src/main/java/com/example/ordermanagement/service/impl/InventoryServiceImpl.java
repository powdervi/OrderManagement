package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.InventoryAction;
import com.example.ordermanagement.dto.request.InventoryCreateReq;
import com.example.ordermanagement.dto.request.InventoryUpdateReq;
import com.example.ordermanagement.entity.Inventory;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.InventoryRepo;
import com.example.ordermanagement.repository.ProductRepo;
import com.example.ordermanagement.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepo inventoryRepo;
    private final ModelMapper modelMapper;
    private final ProductRepo productRepo;

    @Override
    @Transactional
    public Inventory create(String productId, InventoryCreateReq req) {

        productRepo.findById(productId)
                .orElseThrow(() -> new MHException(MHErrors.PRODUCT_NOT_FOUND));

        if (inventoryRepo.existsByProductId(productId)) {
            throw new MHException(MHErrors.INVENTORY_ALREADY_EXISTS);
        }

        Inventory inventory = modelMapper.map(req, Inventory.class);
        inventory.setId(null);
        inventory.setProductId(productId);

        return inventoryRepo.save(inventory);
    }

    @Override
    @Transactional
    public Inventory update(String id, InventoryUpdateReq req) {
        Inventory inventory = inventoryRepo.findById(id)
                .orElseThrow(() -> new MHException(MHErrors.INVENTORY_NOT_FOUND));

        Integer currentStock = inventory.getQuantityInStock();

        if (req.getAction() == InventoryAction.INCREASE) {
            inventory.setQuantityInStock(currentStock + req.getQuantity());
        } else if (req.getAction() == InventoryAction.DECREASE) {
            if (currentStock < req.getQuantity()) {
                throw new MHException(MHErrors.INVENTORY_NOT_ENOUGH);
            }
            inventory.setQuantityInStock(currentStock - req.getQuantity());
        }

        return inventoryRepo.save(inventory);
    }
}

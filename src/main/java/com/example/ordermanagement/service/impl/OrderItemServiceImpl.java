package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.ProductStatus;
import com.example.ordermanagement.dto.request.OrderItemReq;
import com.example.ordermanagement.dto.response.OrderIssueRes;
import com.example.ordermanagement.entity.Inventory;
import com.example.ordermanagement.entity.OrderItem;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.InventoryRepo;
import com.example.ordermanagement.repository.OrderItemRepo;
import com.example.ordermanagement.repository.ProductRepo;
import com.example.ordermanagement.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final OrderItemRepo orderItemRepo;

    @Override
    public ItemMetrics calculateMetricsAndValidateStock(List<OrderItemReq> items) {
        List<String> productIds = items.stream().map(OrderItemReq::getProductId).toList();

        List<Product> products = productRepo.findAllById(productIds);
        if (products.size() != productIds.size()) throw new MHException(MHErrors.PRODUCT_NOT_FOUND);
        Map<String, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        List<Inventory> inventories = inventoryRepo.findAllByProductIdInForUpdate(productIds);
        if (inventories.size() != productIds.size()) throw new MHException(MHErrors.INVENTORY_NOT_FOUND);
        Map<String, Inventory> inventoryMap = inventories.stream().collect(Collectors.toMap(Inventory::getProductId, Function.identity()));

        BigDecimal itemTotal = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (OrderItemReq itemReq : items) {
            Product product = productMap.get(itemReq.getProductId());
            if (product.getStatus() != ProductStatus.ACTIVE) throw new MHException(MHErrors.PRODUCT_INACTIVE);

            Inventory inventory = inventoryMap.get(product.getId());
            if (itemReq.getQuantity() > inventory.getQuantityInStock()) throw new MHException(MHErrors.OUT_OF_STOCK);

            BigDecimal lineTotal = product.getBasePrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            itemTotal = itemTotal.add(lineTotal);

            if (product.getWeight() != null) {
                totalWeight = totalWeight.add(product.getWeight().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
            }
        }
        return new ItemMetrics(itemTotal, totalWeight);
    }

    @Override
    @Transactional
    public void processAndSaveOrderItems(String orderId, List<OrderItemReq> items) {
        List<String> productIds = items.stream().map(OrderItemReq::getProductId).toList();

        Map<String, Product> productMap = productRepo.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        Map<String, Inventory> inventoryMap = inventoryRepo.findAllByProductIdIn(productIds).stream()
                .collect(Collectors.toMap(Inventory::getProductId, Function.identity()));

        List<OrderItem> orderItemsToSave = new ArrayList<>();
        List<Inventory> inventoriesToUpdate = new ArrayList<>();

        for (OrderItemReq itemReq : items) {
            Product product = productMap.get(itemReq.getProductId());
            Inventory inventory = inventoryMap.get(product.getId());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(product.getId());
            orderItem.setProductNameSnapshot(product.getName());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setUnitPrice(product.getBasePrice());
            orderItem.setDiscountAmount(BigDecimal.ZERO);
            orderItem.setFinalUnitPrice(product.getBasePrice());
            orderItem.setLineTotal(product.getBasePrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));

            orderItemsToSave.add(orderItem);
            inventory.setQuantityInStock(inventory.getQuantityInStock() - itemReq.getQuantity());
            inventoriesToUpdate.add(inventory);
        }
        orderItemRepo.saveAll(orderItemsToSave);
        inventoryRepo.saveAll(inventoriesToUpdate);
    }

    private OrderIssueRes createIssue(String type, String message, String productId) {
        OrderIssueRes issue = new OrderIssueRes();
        issue.setType(type);
        issue.setMessage(message);
        issue.setProductId(productId);
        return issue;
    }
}

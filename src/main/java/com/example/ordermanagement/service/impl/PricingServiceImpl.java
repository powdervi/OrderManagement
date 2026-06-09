package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.CarrierStatus;
import com.example.ordermanagement.common.DiscountType;
import com.example.ordermanagement.dto.response.OrderIssueRes;
import com.example.ordermanagement.entity.Carrier;
import com.example.ordermanagement.entity.Promotion;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.CarrierRepo;
import com.example.ordermanagement.repository.PromotionRepo;
import com.example.ordermanagement.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import com.example.ordermanagement.common.ProductStatus;
import com.example.ordermanagement.dto.request.OrderItemReq;
import com.example.ordermanagement.entity.Inventory;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.repository.InventoryRepo;
import com.example.ordermanagement.repository.ProductRepo;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;

    @Override
    public BigDecimal calculateSubtotalForSummary(List<OrderItemReq> items, List<OrderIssueRes> issues) {
        List<String> productIds = items.stream().map(OrderItemReq::getProductId).toList();

        Map<String, Product> productMap = productRepo.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        Map<String, Inventory> inventoryMap = inventoryRepo.findAllByProductIdIn(productIds).stream()
                .collect(Collectors.toMap(Inventory::getProductId, Function.identity()));

        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItemReq item : items) {
            Product product = productMap.get(item.getProductId());
            if (product == null || product.getStatus() != ProductStatus.ACTIVE) {
                issues.add(createIssue("PRODUCT_INACTIVE", "Product inactive", product != null ? product.getId() : null));
                continue;
            }

            Inventory inventory = inventoryMap.get(product.getId());
            if (inventory == null || item.getQuantity() > inventory.getQuantityInStock()) {
                String msg = inventory == null ? "Inventory not found" : "Only " + inventory.getQuantityInStock() + " items left";
                issues.add(createIssue("OUT_OF_STOCK", msg, product.getId()));
                continue;
            }
            subtotal = subtotal.add(product.getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return subtotal;
    }

    private OrderIssueRes createIssue(String type, String message, String productId) {
        OrderIssueRes issue = new OrderIssueRes();
        issue.setType(type);
        issue.setMessage(message);
        issue.setProductId(productId);
        return issue;
    }
}
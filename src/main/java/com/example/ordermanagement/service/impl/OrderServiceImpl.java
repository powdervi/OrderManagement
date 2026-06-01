package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.*;
import com.example.ordermanagement.dto.request.OrderItemReq;
import com.example.ordermanagement.dto.request.OrderSummaryReq;
import com.example.ordermanagement.dto.request.PlaceOrderReq;
import com.example.ordermanagement.dto.response.CarrierRes;
import com.example.ordermanagement.dto.response.OrderIssueRes;
import com.example.ordermanagement.dto.response.OrderSummaryRes;
import com.example.ordermanagement.dto.response.PlaceOrderRes;
import com.example.ordermanagement.entity.*;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.*;
import com.example.ordermanagement.service.OrderService;
import com.example.ordermanagement.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final PromotionRepo promotionRepo;
    private final InventoryRepo inventoryRepo;
    private final ProductRepo productRepo;
    private final CarrierRepo carrierRepo;
    private final OrderItemRepo orderItemRepo;
    private final OrderRepo orderRepo;
    private final AddressRepo addressRepo;
    private final TrackingLogRepo trackingLogRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final ModelMapper modelMapper;


    @Override
    public OrderSummaryRes getSummary(OrderSummaryReq req) {

        validateDuplicateProducts(req.getItems());
        List<OrderIssueRes> issues = new ArrayList<>();

        BigDecimal subtotal = calculateSubtotal(req.getItems(), issues);
        BigDecimal discount = calculateDiscount(subtotal, req.getPromotionId(), issues);

        Carrier carrier = randomCarrier();

        BigDecimal shippingFee = carrier.getShippingFee();

        BigDecimal grandTotal = subtotal.subtract(discount).add(shippingFee);

        CarrierRes carrierRes = modelMapper.map(carrier, CarrierRes.class);

        OrderSummaryRes res = new OrderSummaryRes();
        res.setSubtotal(subtotal);
        res.setDiscount(discount);
        res.setShippingFee(shippingFee);
        res.setGrandTotal(grandTotal);
        res.setCarrier(carrierRes);
        res.setIssues(issues);
        res.setValid(issues.isEmpty());

        return res;
    }

    @Transactional
    @Override
    public PlaceOrderRes placeOrder(PlaceOrderReq req) {

        validateDuplicateProducts(req.getItems());

        String userId = SecurityUtils.getCurrentUserId();

        Address address = addressRepo.findById(req.getAddressId()).orElseThrow(() -> new MHException(MHErrors.ADDRESS_NOT_FOUND));

        if (!address.getUserId().equals(userId)) {
            throw new MHException(MHErrors.ADDRESS_NOT_FOUND);
        }

        PaymentMethod paymentMethod = paymentMethodRepo.findById(req.getPaymentMethodId()).orElseThrow(() -> new MHException(MHErrors.PAYMENT_METHOD_NOT_FOUND));

        if (paymentMethod.getStatus() != PaymentMethodStatus.ACTIVE) {
            throw new MHException(MHErrors.INVALID_PAYMENT_METHOD);
        }

        Carrier carrier = carrierRepo.findById(req.getCarrierId()).orElseThrow(() -> new MHException(MHErrors.CARRIER_NOT_FOUND));

        if (carrier.getStatus() != CarrierStatus.ACTIVE) {
            throw new MHException(MHErrors.CARRIER_NOT_FOUND);
        }

        Promotion promotion = validatePromotion(req.getPromotionId());

        List<String> productIds = req.getItems().stream().map(OrderItemReq::getProductId).toList();
        List<Product> products = productRepo.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new MHException(MHErrors.PRODUCT_NOT_FOUND);
        }
        Map<String, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        List<Inventory> inventories = inventoryRepo.findAllByProductIdInForUpdate(productIds);
        if (inventories.size() != productIds.size()) {
            throw new MHException(MHErrors.INVENTORY_NOT_FOUND);
        }
        Map<String, Inventory> inventoryMap = inventories.stream().collect(Collectors.toMap(Inventory::getProductId, Function.identity()));

        BigDecimal itemTotal = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (OrderItemReq itemReq : req.getItems()) {
            Product product = productMap.get(itemReq.getProductId());
            if (product == null) {
                throw new MHException(MHErrors.PRODUCT_NOT_FOUND);
            }
            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new MHException(MHErrors.PRODUCT_INACTIVE);
            }

            Inventory inventory = inventoryMap.get(product.getId());
            if (inventory == null) {
                throw new MHException(MHErrors.INVENTORY_NOT_FOUND);
            }
            if (itemReq.getQuantity() > inventory.getQuantityInStock()) {
                throw new MHException(MHErrors.OUT_OF_STOCK);
            }

            BigDecimal lineTotal = product.getBasePrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            itemTotal = itemTotal.add(lineTotal);

            if (product.getWeight() != null) {
                totalWeight = totalWeight.add(product.getWeight().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
            }

        }

        BigDecimal discountTotal = calculateDiscount(itemTotal, req.getPromotionId(), new ArrayList<>());
        BigDecimal shippingFee = carrier.getShippingFee();
        BigDecimal grandTotal = itemTotal.subtract(discountTotal).add(shippingFee);

        Order order = new Order();
        order.setOrderCode(generateOrderCode());
        order.setTrackingNumber(generateTrackingNumber());
        order.setUserId(userId);
        order.setAddressId(address.getId());
        order.setPaymentMethodId(paymentMethod.getId());
        order.setPromotionId(promotion == null ? null : promotion.getId());
        order.setCarrierId(carrier.getId());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setItemTotal(itemTotal);
        order.setDiscountTotal(discountTotal);
        order.setShippingFee(shippingFee);
        order.setGrandTotal(grandTotal);
        order.setTotalWeight(totalWeight);
        order.setNote(req.getNote());
        order.setPlacedAt(LocalDateTime.now());
        order.setEstimatedDeliveryAt(LocalDateTime.now().plusDays(3));

        // address snapshot
        order.setRecipientNameSnapshot(address.getRecipientName());
        order.setRecipientPhoneSnapshot(address.getRecipientPhone());
        order.setProvinceSnapshot(address.getProvince());
        order.setDistrictSnapshot(address.getDistrict());
        order.setWardSnapshot(address.getWard());
        order.setStreetSnapshot(address.getStreet());
        order.setDetailSnapshot(address.getDetail());
        order.setPostalCodeSnapshot(address.getPostalCode());

        // payment snapshot
        order.setPaymentMethodNameSnapshot(paymentMethod.getName());

        // carrier snapshot
        order.setCarrierNameSnapshot(carrier.getName());

        // promotion snapshot
        if (promotion != null) {
            order.setPromotionCodeSnapshot(promotion.getCode());
        }

        orderRepo.save(order);

        for (OrderItemReq itemReq : req.getItems()) {
            Product product = productMap.get(itemReq.getProductId());
            if (product == null) {
                throw new MHException(MHErrors.PRODUCT_NOT_FOUND);
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setProductNameSnapshot(product.getName());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setUnitPrice(product.getBasePrice());
            orderItem.setDiscountAmount(BigDecimal.ZERO);
            orderItem.setFinalUnitPrice(product.getBasePrice());
            orderItem.setLineTotal(product.getBasePrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
            orderItemRepo.save(orderItem);

            Inventory inventory = inventoryMap.get(product.getId());
            if (inventory == null) {
                throw new MHException(MHErrors.INVENTORY_NOT_FOUND);
            }
            inventory.setQuantityInStock(inventory.getQuantityInStock() - itemReq.getQuantity());
            inventoryRepo.save(inventory);
        }

        if (req.getSource() == OrderSource.CART) {
            removeCartItems(userId, req.getItems());
        }

        TrackingLog trackingLog = new TrackingLog();

        trackingLog.setOrderId(order.getId());
        trackingLog.setStatus(OrderStatus.PENDING);
        trackingLog.setNote("Order created");
        trackingLog.setChangedByUserId(userId);
        trackingLog.setChangedAt(LocalDateTime.now());

        trackingLogRepo.save(trackingLog);

        PlaceOrderRes res = new PlaceOrderRes();
        res.setOrderId(order.getId());
        res.setOrderCode(order.getOrderCode());
        res.setTrackingNumber(order.getTrackingNumber());

        return res;
    }

    private void removeCartItems(String userId, List<OrderItemReq> items) {

        Cart cart = cartRepo.findByUserId(userId).orElseThrow(() -> new MHException(MHErrors.CART_NOT_FOUND));
        List<String> productIds = items.stream().map(OrderItemReq::getProductId).toList();
        List<CartItem> cartItems = cartItemRepo.findAllByCartIdAndProductIdIn(cart.getId(), productIds);
        cartItemRepo.deleteAll(cartItems);
    }

    private Promotion validatePromotion(String promotionId) {
        if (promotionId == null || promotionId.isBlank()) {
            return null;
        }
        Promotion promotion = promotionRepo.findById(promotionId).orElseThrow(() -> new MHException(MHErrors.PROMOTION_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(promotion.getStartAt()) || now.isAfter(promotion.getEndAt())) {
            throw new MHException(MHErrors.INVALID_PROMOTION);
        }
        return promotion;
    }

    private String generateOrderCode() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }

    private BigDecimal calculateSubtotal(List<OrderItemReq> items, List<OrderIssueRes> issues) {

        BigDecimal subtotal = BigDecimal.ZERO;

        List<String> productIds = items.stream().map(OrderItemReq::getProductId).toList();
        List<Product> products = productRepo.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new MHException(MHErrors.PRODUCT_NOT_FOUND);
        }
        Map<String, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        List<Inventory> inventories = inventoryRepo.findAllById(productIds);
        if (inventories.size() != productIds.size()) {
            throw new MHException(MHErrors.INVENTORY_NOT_FOUND);
        }
        Map<String, Inventory> inventoryMap = inventories.stream().collect(Collectors.toMap(Inventory::getProductId, Function.identity()));

        for (OrderItemReq item : items) {
            Product product = productMap.get(item.getProductId());
            if (product == null) {
                throw new MHException(MHErrors.PRODUCT_NOT_FOUND);
            }
            if (product.getStatus() != ProductStatus.ACTIVE) {
                OrderIssueRes issue = new OrderIssueRes();
                issue.setProductId(product.getId());
                issue.setType("PRODUCT_INACTIVE");
                issue.setMessage("Product inactive");
                issues.add(issue);

                continue;
            }

            Inventory inventory = inventoryMap.get(product.getId());
            if (inventory == null) {
                throw new MHException(MHErrors.INVENTORY_NOT_FOUND);
            }

            if (item.getQuantity() > inventory.getQuantityInStock()) {
                OrderIssueRes issue = new OrderIssueRes();
                issue.setProductId(product.getId());
                issue.setType("OUT_OF_STOCK");
                issue.setMessage("Only " + inventory.getQuantityInStock() + " items left");
                issues.add(issue);
            }

            BigDecimal lineTotal = product.getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            subtotal = subtotal.add(lineTotal);

        }

        return subtotal;
    }

    private void validateDuplicateProducts(List<OrderItemReq> items) {
        Set<String> productIds = new HashSet<>();
        for (OrderItemReq item : items) {
            if (!productIds.add(item.getProductId())) {
                throw new MHException(MHErrors.DUPLICATE_PRODUCT);
            }
        }
    }

    private BigDecimal calculateDiscount(BigDecimal subtotal, String promotionId, List<OrderIssueRes> issues) {

        if (promotionId == null || promotionId.isBlank()) {
            return BigDecimal.ZERO;
        }

        Promotion promotion = promotionRepo.findById(promotionId).orElse(null);

        if (promotion == null) {
            OrderIssueRes issue = new OrderIssueRes();
            issue.setType("INVALID_PROMOTION");
            issue.setMessage("Promotion not found");
            issues.add(issue);
            return BigDecimal.ZERO;
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(promotion.getStartAt()) || now.isAfter(promotion.getEndAt())) {
            OrderIssueRes issue = new OrderIssueRes();
            issue.setType("INVALID_PROMOTION");
            issue.setMessage("Promotion expired");
            issues.add(issue);
            return BigDecimal.ZERO;
        }

        BigDecimal discount;

        if (promotion.getDiscountType() == DiscountType.PERCENT) {
            discount = subtotal.multiply(promotion.getDiscountValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        } else {
            discount = promotion.getDiscountValue();
        }

        if (discount.compareTo(subtotal) > 0) {
            discount = subtotal;
        }

        return discount;

    }

    private Carrier randomCarrier() {
        List<Carrier> carriers = carrierRepo.findAllByStatus(CarrierStatus.ACTIVE);
        if (carriers.isEmpty()) {
            throw new MHException(MHErrors.CARRIER_NOT_FOUND);
        }
        int index = ThreadLocalRandom.current().nextInt(carriers.size());
        return carriers.get(index);
    }

}

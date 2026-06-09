package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.*;
import com.example.ordermanagement.dto.request.OrderSummaryReq;
import com.example.ordermanagement.dto.request.PlaceOrderReq;
import com.example.ordermanagement.dto.request.UpdateOrderStatusReq;
import com.example.ordermanagement.dto.response.*;
import com.example.ordermanagement.entity.*;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.mapper.OrderMapper;
import com.example.ordermanagement.repository.*;
import com.example.ordermanagement.service.*;
import com.example.ordermanagement.service.helper.OrderCodeGenerator;
import com.example.ordermanagement.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final UserRepo userRepo;
    private final OrderMapper orderMapper;

    private final AddressService addressService;
    private final PaymentMethodService paymentMethodService;
    private final OrderValidationService validationService;
    private final CarrierService carrierService;
    private final PromotionService promotionService;
    private final PricingService pricingService;
    private final OrderItemService orderItemService;
    private final TrackingLogService trackingLogService;
    private final NotificationService notificationService;
    private final CartCleanupService cartCleanupService;
    private final OrderCodeGenerator codeGenerator;

    @Override
    public OrderSummaryRes getSummary(OrderSummaryReq req) {
        validationService.validateDuplicateProducts(req.getItems());
        List<OrderIssueRes> issues = new ArrayList<>();

        BigDecimal subtotal = pricingService.calculateSubtotalForSummary(req.getItems(), issues);

        Promotion promotion = null;
        if (req.getPromotionId() != null && !req.getPromotionId().isBlank()) {
            try {
                promotion = promotionService.validatePromotion(req.getPromotionId());
            } catch (Exception e) {
                OrderIssueRes issue = new OrderIssueRes();
                issue.setType("INVALID_PROMOTION");
                issue.setMessage("Promotion not found or expired");
                issues.add(issue);
            }
        }

        BigDecimal discount = promotionService.calculateDiscount(subtotal, promotion, issues);

        Carrier carrier = carrierService.getRandomCarrier();
        BigDecimal shippingFee = carrier.getShippingFee();
        BigDecimal grandTotal = subtotal.subtract(discount).add(shippingFee);

        OrderSummaryRes res = new OrderSummaryRes();
        res.setSubtotal(subtotal);
        res.setDiscount(discount);
        res.setShippingFee(shippingFee);
        res.setGrandTotal(grandTotal);
        res.setCarrier(orderMapper.toCarrierRes(carrier));
        res.setIssues(issues);
        res.setValid(issues.isEmpty());

        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PlaceOrderRes placeOrder(PlaceOrderReq req) {
        validationService.validateDuplicateProducts(req.getItems());
        String userId = SecurityUtils.getCurrentUserId();

        Address address = addressService.validateAddress(req.getAddressId(), userId);
        PaymentMethod paymentMethod = paymentMethodService.validatePaymentMethod(req.getPaymentMethodId());
        Carrier carrier = carrierService.validateAndGetCarrier(req.getCarrierId());

        Promotion promotion = promotionService.validatePromotion(req.getPromotionId());

        OrderItemService.ItemMetrics metrics = orderItemService.calculateMetricsAndValidateStock(req.getItems());

        BigDecimal discountTotal = promotionService.calculateDiscount(metrics.getItemTotal(), promotion, new ArrayList<>());
        BigDecimal shippingFee = carrier.getShippingFee();
        BigDecimal grandTotal = metrics.getItemTotal().subtract(discountTotal).add(shippingFee);

        Order order = createOrder(req, userId, address, paymentMethod, carrier, promotion, metrics, discountTotal, shippingFee, grandTotal);
        orderRepo.save(order);

        orderItemService.processAndSaveOrderItems(order.getId(), req.getItems());

        if (req.getSource() == OrderSource.CART) {
            cartCleanupService.removeCartItems(userId, req.getItems());
        }

        trackingLogService.logTracking(order.getId(), OrderStatus.PENDING, "Order created", null, userId);
        notificationService.createNotification(order, OrderStatus.PENDING);

        PlaceOrderRes res = new PlaceOrderRes();
        res.setOrderId(order.getId());
        res.setOrderCode(order.getOrderCode());
        res.setTrackingNumber(order.getTrackingNumber());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String orderId, UpdateOrderStatusReq req) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new MHException(MHErrors.ORDER_NOT_FOUND));
        String userId = SecurityUtils.getCurrentUserId();

        OrderStatus currentStatus = order.getOrderStatus();
        OrderStatus newStatus = req.getStatus();

        validationService.validateTransition(currentStatus, newStatus);
        validationService.validateRolePermission(userId, currentStatus, newStatus);

        order.setOrderStatus(newStatus);
        orderRepo.save(order);

        trackingLogService.logTracking(order.getId(), newStatus, req.getNote(), req.getLocation(), userId);
        notificationService.createNotification(order, newStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailRes getOrderDetail(String orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new MHException(MHErrors.ORDER_NOT_FOUND));
        validationService.validateOrderAccess(order);

        List<OrderItem> orderItems = orderItemRepo.findByOrderId(orderId);

        OrderDetailRes res = orderMapper.toOrderDetailRes(order);
        res.setItems(orderMapper.toItemResList(orderItems));

        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public TrackingTimelineRes getTrackingTimeline(String orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new MHException(MHErrors.ORDER_NOT_FOUND));
        validationService.validateOrderAccess(order);

        List<TrackingLog> trackingLogs = trackingLogService.getTrackingLogs(orderId);
        Set<String> userIds = trackingLogs.stream().map(TrackingLog::getChangedByUserId).filter(Objects::nonNull).collect(Collectors.toSet());

        Map<String, String> userNameMap = userRepo.findAllById(userIds).stream().collect(Collectors.toMap(User::getId, u -> u.getLastName() + " " + u.getFirstName()));

        List<TrackingEventRes> events = trackingLogs.stream()
                .map(log -> orderMapper.toTrackingEventRes(log, userNameMap.getOrDefault(log.getChangedByUserId(), "System")))
                .toList();
        TrackingTimelineRes res = new TrackingTimelineRes();
        res.setEvents(events);
        return res;
    }

    public Order createOrder(PlaceOrderReq req, String userId, Address address, PaymentMethod paymentMethod,
                             Carrier carrier, Promotion promotion, OrderItemService.ItemMetrics metrics,
                             BigDecimal discountTotal, BigDecimal shippingFee, BigDecimal grandTotal) {

        LocalDateTime now = LocalDateTime.now();

        return Order.builder()
                .orderCode(codeGenerator.generateOrderCode())
                .trackingNumber(codeGenerator.generateTrackingNumber())
                .userId(userId)
                .placedAt(now)
                .estimatedDeliveryAt(now.plusDays(3))
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.UNPAID)
                .note(req.getNote())

                .addressId(address.getId())
                .paymentMethodId(paymentMethod.getId())
                .carrierId(carrier.getId())
                .promotionId(promotion != null ? promotion.getId() : null)

                .itemTotal(metrics.getItemTotal())
                .totalWeight(metrics.getTotalWeight())
                .discountTotal(discountTotal)
                .shippingFee(shippingFee)
                .grandTotal(grandTotal)

                .recipientNameSnapshot(address.getRecipientName())
                .recipientPhoneSnapshot(address.getRecipientPhone())
                .provinceSnapshot(address.getProvince())
                .districtSnapshot(address.getDistrict())
                .wardSnapshot(address.getWard())
                .streetSnapshot(address.getStreet())
                .detailSnapshot(address.getDetail())
                .postalCodeSnapshot(address.getPostalCode())
                .paymentMethodNameSnapshot(paymentMethod.getName())
                .carrierNameSnapshot(carrier.getName())
                .promotionCodeSnapshot(promotion != null ? promotion.getCode() : null)

                .build();
    }
}

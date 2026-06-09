package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.common.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDetailRes {
    private String orderId;
    private String orderCode;
    private String trackingNumber;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime placedAt;
    private LocalDateTime estimatedDeliveryAt;
    private BigDecimal itemTotal;
    private BigDecimal discountTotal;
    private BigDecimal shippingFee;
    private BigDecimal grandTotal;
    private String note;
    private AddressSnapshotRes address;
    private PaymentInfoRes payment;
    private CarrierInfoRes carrier;
    private List<OrderItemDetailRes> items;
}

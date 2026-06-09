package com.example.ordermanagement.entity;

import com.example.ordermanagement.common.OrderStatus;
import com.example.ordermanagement.common.PaymentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends AbstractEntity {

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "address_id")
    private String addressId;

    @Column(name = "payment_method_id")
    private String paymentMethodId;

    @Column(name = "promotion_id")
    private String promotionId;

    @Column(name = "carrier_id")
    private String carrierId;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "item_total")
    private BigDecimal itemTotal;

    @Column(name = "discount_total")
    private BigDecimal discountTotal;

    @Column(name = "shipping_fee")
    private BigDecimal shippingFee;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Column(name = "total_weight")
    private BigDecimal totalWeight;

    @Column(name = "note")
    private String note;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "placed_at")
    private LocalDateTime placedAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "estimated_delivery_at")
    private LocalDateTime estimatedDeliveryAt;

    @Column(name = "recipient_name_snapshot")
    private String recipientNameSnapshot;

    @Column(name = "recipient_phone_snapshot")
    private String recipientPhoneSnapshot;

    @Column(name = "province_snapshot")
    private String provinceSnapshot;

    @Column(name = "district_snapshot")
    private String districtSnapshot;

    @Column(name = "ward_snapshot")
    private String wardSnapshot;

    @Column(name = "street_snapshot")
    private String streetSnapshot;

    @Column(name = "detail_snapshot")
    private String detailSnapshot;

    @Column(name = "postal_code_snapshot")
    private String postalCodeSnapshot;

    @Column(name = "payment_method_name_snapshot")
    private String paymentMethodNameSnapshot;

    @Column(name = "carrier_name_snapshot")
    private String carrierNameSnapshot;

    @Column(name = "promotion_code_snapshot")
    private String promotionCodeSnapshot;
}

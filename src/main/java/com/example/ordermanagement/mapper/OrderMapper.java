package com.example.ordermanagement.mapper;

import com.example.ordermanagement.dto.response.*;
import com.example.ordermanagement.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    CarrierRes toCarrierRes(Carrier carrier);

    @Mapping(source = "recipientNameSnapshot", target = "recipientName")
    @Mapping(source = "recipientPhoneSnapshot", target = "recipientPhone")
    @Mapping(source = "provinceSnapshot", target = "province")
    @Mapping(source = "districtSnapshot", target = "district")
    @Mapping(source = "wardSnapshot", target = "ward")
    @Mapping(source = "streetSnapshot", target = "street")
    @Mapping(source = "detailSnapshot", target = "detail")
    @Mapping(source = "postalCodeSnapshot", target = "postalCode")
    AddressSnapshotRes toAddressRes(Order order);

    @Mapping(source = "paymentMethodNameSnapshot", target = "paymentMethodName")
    PaymentInfoRes toPaymentRes(Order order);

    @Mapping(source = "carrierNameSnapshot", target = "carrierName")
    CarrierInfoRes toCarrierRes(Order order);

    @Mapping(source = "productNameSnapshot", target = "productName")
    OrderItemDetailRes toItemRes(OrderItem orderItem);

    List<OrderItemDetailRes> toItemResList(List<OrderItem> items);

    @Mapping(source = "id", target = "orderId")
    @Mapping(target = "address", expression = "java(toAddressRes(order))")
    @Mapping(target = "payment", expression = "java(toPaymentRes(order))")
    @Mapping(target = "carrier", expression = "java(toCarrierRes(order))")
    @Mapping(target = "items", ignore = true)
    OrderDetailRes toOrderDetailRes(Order order);

    @Mapping(source = "log.status", target = "status")
    @Mapping(source = "log.note", target = "note")
    @Mapping(source = "log.location", target = "location")
    @Mapping(source = "log.changedAt", target = "changedAt")
    @Mapping(source = "userName", target = "changedBy")
    TrackingEventRes toTrackingEventRes(TrackingLog log, String userName);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "recipientNameSnapshot", target = "customerName")
    @Mapping(source = "grandTotal", target = "amount")
    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "carrierNameSnapshot", target = "assignedCarrier", defaultValue = "Unassigned")
    RecentOrderRes toRecentOrderRes(Order order);
}

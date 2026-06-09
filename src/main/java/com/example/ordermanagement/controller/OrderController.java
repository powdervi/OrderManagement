package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.OrderSummaryReq;
import com.example.ordermanagement.dto.request.PlaceOrderReq;
import com.example.ordermanagement.dto.request.UpdateOrderStatusReq;
import com.example.ordermanagement.dto.response.OrderDetailRes;
import com.example.ordermanagement.dto.response.OrderSummaryRes;
import com.example.ordermanagement.dto.response.PlaceOrderRes;
import com.example.ordermanagement.dto.response.TrackingTimelineRes;
import com.example.ordermanagement.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //todo 3: Giỏ hàng & Kiểm tra tồn kho: api summary
    @PostMapping("/summary")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BaseResponse<OrderSummaryRes>> getSummary(@RequestBody @Valid OrderSummaryReq request) {
        OrderSummaryRes result = orderService.getSummary(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(result));
    }

    // todo 4: Giỏ hàng & Kiểm tra tồn kho: api place order
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BaseResponse<PlaceOrderRes>> placeOrder(@RequestBody @Valid PlaceOrderReq request) {
        PlaceOrderRes result = orderService.placeOrder(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(result));
    }

    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_STAFF','SHIPPER')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<BaseResponse<Void>> updateStatus(@PathVariable String orderId, @Valid @RequestBody UpdateOrderStatusReq req) {
        orderService.updateStatus(orderId, req);
        return ResponseEntity.ok(BaseResponse.ofSuccess("Order status has been updated successfully"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','WAREHOUSE_STAFF','SHIPPER')")
    @GetMapping("/{orderId}/detail")
    public ResponseEntity<BaseResponse<OrderDetailRes>> getOrderDetail(@PathVariable String orderId) {
        return ResponseEntity.ok(BaseResponse.ofSuccess(orderService.getOrderDetail(orderId)));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','WAREHOUSE_STAFF','SHIPPER')")
    @GetMapping("/{orderId}/tracking")
    public ResponseEntity<BaseResponse<TrackingTimelineRes>> getTrackingTimeline(@PathVariable String orderId) {
        return ResponseEntity.ok(BaseResponse.ofSuccess(orderService.getTrackingTimeline(orderId)));
    }
}

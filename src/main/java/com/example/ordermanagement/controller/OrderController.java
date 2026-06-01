package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.OrderSummaryReq;
import com.example.ordermanagement.dto.request.PlaceOrderReq;
import com.example.ordermanagement.dto.response.OrderSummaryRes;
import com.example.ordermanagement.dto.response.PlaceOrderRes;
import com.example.ordermanagement.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //todo 3: Giỏ hàng & Kiểm tra tồn kho: api summary
    @PostMapping("/summary")
    public ResponseEntity<BaseResponse<OrderSummaryRes>> getSummary(@RequestBody @Valid OrderSummaryReq request) {
        OrderSummaryRes result = orderService.getSummary(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(result));
    }

    // todo 4: Giỏ hàng & Kiểm tra tồn kho: api place order
    @PostMapping
    public ResponseEntity<BaseResponse<PlaceOrderRes>> placeOrder(@RequestBody @Valid PlaceOrderReq request) {
        PlaceOrderRes result = orderService.placeOrder(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(result));
    }
}

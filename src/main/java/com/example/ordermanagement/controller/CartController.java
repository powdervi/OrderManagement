package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.response.CartRes;
import com.example.ordermanagement.service.CartService;
import com.example.ordermanagement.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CartController {
    private final CartService cartService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/carts/my-cart")
    public ResponseEntity<BaseResponse<CartRes>> createCart() {
        String userId = SecurityUtils.getCurrentUserId();
        CartRes res = cartService.create(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ofSuccess(res));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/carts/my-cart")
    public ResponseEntity<BaseResponse<CartRes>> getCart() {
        String userId = SecurityUtils.getCurrentUserId();
        CartRes res = cartService.getByUserId(userId);
        return ResponseEntity.ok(BaseResponse.ofSuccess(res));
    }
}

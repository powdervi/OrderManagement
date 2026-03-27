package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.response.CartRes;
import com.example.ordermanagement.entity.Cart;
import com.example.ordermanagement.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CartController {
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @PostMapping("/users/{userId}/cart")
    public ResponseEntity<BaseResponse<CartRes>> createCart(@PathVariable String userId) {
        Cart cart = cartService.create(userId);
        CartRes res = modelMapper.map(cart, CartRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(res));
    }

    @GetMapping("/users/{userId}/cart")
    public ResponseEntity<BaseResponse<CartRes>> getCart(@PathVariable String userId) {
        Cart cart = cartService.getByUserId(userId);
        CartRes res = modelMapper.map(cart, CartRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(res));
    }
}

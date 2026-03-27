package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.CartItemCreateReq;
import com.example.ordermanagement.dto.response.CartItemDetailRes;
import com.example.ordermanagement.dto.response.CartItemRes;
import com.example.ordermanagement.entity.CartItem;
import com.example.ordermanagement.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CartItemController {
    private final CartItemService cartItemService;
    private final ModelMapper modelMapper;

    @PostMapping("/users/{userId}/cart/item")
    public ResponseEntity<BaseResponse<CartItemRes>> addCartItem(@PathVariable String userId,
                                                                 @RequestBody @Valid CartItemCreateReq cartItemCreateReq){
        CartItem cartItem = cartItemService.creatItem(userId, cartItemCreateReq);
        CartItemRes cartItemRes = modelMapper.map(cartItem, CartItemRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(cartItemRes));
    }

    @GetMapping("/users/{userId}/cart/items")
    public ResponseEntity<BaseResponse<List<CartItemDetailRes>>> getCartItems(
            @PathVariable String userId
    ) {
        List<CartItemDetailRes> res = cartItemService.getCartItems(userId);
        return ResponseEntity.ok(BaseResponse.ofSuccess(res));
    }
}

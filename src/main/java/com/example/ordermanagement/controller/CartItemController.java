package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.CartItemCreateReq;
import com.example.ordermanagement.dto.response.CartItemDetailRes;
import com.example.ordermanagement.dto.response.CartItemRes;
import com.example.ordermanagement.service.CartItemService;
import com.example.ordermanagement.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CartItemController {
    private final CartItemService cartItemService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/carts/my-cart/items")
    public ResponseEntity<BaseResponse<CartItemRes>> addCartItem(@RequestBody @Valid CartItemCreateReq cartItemCreateReq){
        String userId = SecurityUtils.getCurrentUserId();
        CartItemRes cartItem = cartItemService.creatItem(userId, cartItemCreateReq);
        return ResponseEntity.ok(BaseResponse.ofSuccess(cartItem));
    }

    // todo 2 : api get list cart items (man hinh gio hang & kiem tra ton kho)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping("/carts/my-cart/items")
    public ResponseEntity<BaseResponse<List<CartItemDetailRes>>> getCartItems() {
        List<CartItemDetailRes> res = cartItemService.getCartItems();
        return ResponseEntity.ok(BaseResponse.ofSuccess(res));
    }
}

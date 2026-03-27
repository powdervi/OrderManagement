package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.CartItemCreateReq;
import com.example.ordermanagement.dto.response.CartItemDetailRes;
import com.example.ordermanagement.entity.CartItem;
import jakarta.validation.Valid;

import java.util.List;

public interface CartItemService {
    CartItem creatItem(String userId, @Valid CartItemCreateReq cartItemCreateReq);

    List<CartItemDetailRes> getCartItems(String userId);
}

package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.CartSummaryReq;
import com.example.ordermanagement.dto.response.CartSummaryRes;
import com.example.ordermanagement.entity.Cart;

public interface CartService {
    Cart create(String userId);

    Cart getByUserId(String userId);

    CartSummaryRes getSummary(CartSummaryReq req);
}

package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.response.CartRes;

public interface CartService {
    CartRes create(String userId);
    CartRes getByUserId(String userId);
}

package com.example.ordermanagement.service;

import com.example.ordermanagement.entity.Cart;

public interface CartService {
    Cart create(String userId);

    Cart getByUserId(String userId);
}

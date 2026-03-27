package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.entity.Cart;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.CartRepo;
import com.example.ordermanagement.repository.UserRepo;
import com.example.ordermanagement.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public Cart create(String userId) {
        if (!userRepo.existsById(userId)) {
            throw new MHException(MHErrors.USER_NOT_FOUND);
        }

        if (cartRepo.existsByUserId(userId)) {
            throw new MHException(MHErrors.CART_ALREADY_EXISTS);
        }

        Cart cart = new Cart();
        cart.setUserId(userId);

        return cartRepo.save(cart);
    }

    @Override
    public Cart getByUserId(String userId) {
        if (!userRepo.existsById(userId)) {
            throw new MHException(MHErrors.USER_NOT_FOUND);
        }

        return cartRepo.findByUserId(userId)
                .orElseThrow(() -> new MHException(MHErrors.CART_NOT_FOUND));
    }
}

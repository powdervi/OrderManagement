package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.dto.response.CartRes;
import com.example.ordermanagement.entity.*;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.mapper.CartMapper;
import com.example.ordermanagement.repository.*;
import com.example.ordermanagement.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final CartMapper cartMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartRes create(String userId) {
        if (!userRepo.existsById(userId)) {
            throw new MHException(MHErrors.USER_NOT_FOUND);
        }

        if (cartRepo.existsByUserId(userId)) {
            throw new MHException(MHErrors.CART_ALREADY_EXISTS);
        }

        Cart cart = new Cart();
        cart.setUserId(userId);

        Cart cart1 = cartRepo.save(cart);
        return cartMapper.toCartRes(cart1);
    }

    @Transactional(readOnly = true)
    @Override
    public CartRes getByUserId(String userId) {
        if (!userRepo.existsById(userId)) {
            throw new MHException(MHErrors.USER_NOT_FOUND);
        }

        Cart cart = cartRepo.findByUserId(userId).orElseThrow(() -> new MHException(MHErrors.CART_NOT_FOUND));
        return cartMapper.toCartRes(cart);
    }

}

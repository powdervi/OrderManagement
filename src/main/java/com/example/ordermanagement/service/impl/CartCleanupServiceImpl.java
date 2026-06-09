package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.dto.request.OrderItemReq;
import com.example.ordermanagement.entity.Cart;
import com.example.ordermanagement.entity.CartItem;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.CartItemRepo;
import com.example.ordermanagement.repository.CartRepo;
import com.example.ordermanagement.service.CartCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartCleanupServiceImpl implements CartCleanupService {

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;

    @Override
    @Transactional
    public void removeCartItems(String userId, List<OrderItemReq> items) {
        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new MHException(MHErrors.CART_NOT_FOUND));

        List<String> productIds = items.stream().map(OrderItemReq::getProductId).toList();
        List<CartItem> cartItems = cartItemRepo.findAllByCartIdAndProductIdIn(cart.getId(), productIds);

        cartItemRepo.deleteAll(cartItems);
    }
}

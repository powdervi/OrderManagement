package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.dto.request.CartItemCreateReq;
import com.example.ordermanagement.dto.response.CartItemDetailRes;
import com.example.ordermanagement.dto.response.CartItemRes;
import com.example.ordermanagement.entity.Cart;
import com.example.ordermanagement.entity.CartItem;
import com.example.ordermanagement.entity.Inventory;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.mapper.CartItemMapper;
import com.example.ordermanagement.repository.CartItemRepo;
import com.example.ordermanagement.repository.CartRepo;
import com.example.ordermanagement.repository.InventoryRepo;
import com.example.ordermanagement.repository.ProductRepo;
import com.example.ordermanagement.service.CartItemService;
import com.example.ordermanagement.service.InventoryService;
import com.example.ordermanagement.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepo cartItemRepo;
    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final CartItemMapper cartItemMapper;
    private final InventoryService inventoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItemRes creatItem(String userId, CartItemCreateReq req) {

        Cart cart = getCartEntity(userId);
        inventoryService.checkAvailableStock(req.getProductId(), req.getQuantity());

        Optional<CartItem> optional = cartItemRepo.findByCartIdAndProductId(cart.getId(), req.getProductId());
        CartItem savedCartItem;

        if (optional.isPresent()) {
            CartItem existingItem = optional.get();
            existingItem.setQuantity(existingItem.getQuantity() + req.getQuantity());
            savedCartItem = cartItemRepo.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getId());
            newItem.setProductId(req.getProductId());
            newItem.setQuantity(req.getQuantity());
            savedCartItem = cartItemRepo.save(newItem);
        }

        return cartItemMapper.toCartItemRes(savedCartItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemDetailRes> getCartItems() {
        String userId = SecurityUtils.getCurrentUserId();
        Cart cart = getCartEntity(userId);

        List<CartItem> cartItems = cartItemRepo.findAllByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> productIds = cartItems.stream().map(CartItem::getProductId).toList();

        Map<String, Product> productMap = productRepo.findAllById(productIds).stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        return cartItems.stream().map(cartItem -> {
            Product product = productMap.get(cartItem.getProductId());
            if (product == null) {
                throw new MHException(MHErrors.PRODUCT_NOT_FOUND);
            }
            return cartItemMapper.toCartItemDetailRes(cartItem, product);
        }).toList();
    }

    private Cart getCartEntity(String userId) {
        return cartRepo.findByUserId(userId).orElseThrow(() -> new MHException(MHErrors.CART_NOT_FOUND));
    }

}

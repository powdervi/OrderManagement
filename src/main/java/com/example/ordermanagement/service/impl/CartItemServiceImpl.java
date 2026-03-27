package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.dto.request.CartItemCreateReq;
import com.example.ordermanagement.dto.response.CartItemDetailRes;
import com.example.ordermanagement.entity.Cart;
import com.example.ordermanagement.entity.CartItem;
import com.example.ordermanagement.entity.Inventory;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.CartItemRepo;
import com.example.ordermanagement.repository.CartRepo;
import com.example.ordermanagement.repository.InventoryRepo;
import com.example.ordermanagement.repository.ProductRepo;
import com.example.ordermanagement.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepo cartItemRepo;
    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;

    @Override
    @Transactional
    public CartItem creatItem(String userId, CartItemCreateReq req) {

        Cart cart = getCartEntity( userId);

        if (!productRepo.existsById(req.getProductId())) {
            throw new MHException(MHErrors.PRODUCT_NOT_FOUND);
        }

        Inventory inventory = inventoryRepo.findByProductId(req.getProductId())
                .orElseThrow(() -> new MHException(MHErrors.INVENTORY_NOT_FOUND));

        if(req.getQuantity() > inventory.getQuantityInStock()) {
            throw new MHException(MHErrors.OVER_STOCK);
        }

        Optional<CartItem> optional = cartItemRepo
                .findByCartIdAndProductId(cart.getId(), req.getProductId());

        if (optional.isPresent()) {
            CartItem cartItem = optional.get();
            cartItem.setQuantity(cartItem.getQuantity() + req.getQuantity());
            return cartItemRepo.save(cartItem);
        }

        CartItem cartItem = new CartItem();
        cartItem.setId(null);
        cartItem.setCartId(cart.getId());
        cartItem.setProductId(req.getProductId());
        cartItem.setQuantity(req.getQuantity());

        return cartItemRepo.save(cartItem);
    }

    @Override
    public List<CartItemDetailRes> getCartItems(String userId) {
        Cart cart = getCartEntity(userId);

        List<CartItem> cartItems = cartItemRepo.findAllByCartId(cart.getId());

        List<CartItemDetailRes> result = new ArrayList<>();

        if(!cartItems.isEmpty()) {
            for (CartItem cartItem : cartItems) {
                Product product = productRepo.findById(cartItem.getProductId())
                        .orElseThrow(() -> new MHException(MHErrors.PRODUCT_NOT_FOUND));

                CartItemDetailRes res = new CartItemDetailRes();
                res.setCartItemId(cartItem.getId());
                res.setProductId(product.getId());
                res.setProductName(product.getName());
                res.setDescription(product.getDescription());
                res.setBasePrice(product.getBasePrice());
                res.setQuantity(cartItem.getQuantity());
                res.setLineTotal(
                        product.getBasePrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                );

                result.add(res);
            }
        }

        return result;
    }

    private Cart getCartEntity(String userId) {
        return cartRepo.findByUserId(userId)
                .orElseThrow(() -> new MHException(MHErrors.CART_NOT_FOUND));
    }

}

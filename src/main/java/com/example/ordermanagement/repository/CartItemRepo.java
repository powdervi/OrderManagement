package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByCartIdAndProductId(String cartId, String productId);

    List<CartItem> findAllByCartId(String id);

    List<CartItem> findAllByIdInAndCartId(List<String> cartItemIds, String id);

    List<CartItem> findAllByCartIdAndProductIdIn(String cartId, List<String> productIds);
}

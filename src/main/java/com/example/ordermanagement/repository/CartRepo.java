package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {
    boolean existsByUserId(String userId);

    Optional<Cart> findByUserId(String userId);
}

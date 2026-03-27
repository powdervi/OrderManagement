package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Inventory;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, String> {
    boolean existsByProductId(String productId);

    Optional<Inventory> findByProductId( String productId);
}

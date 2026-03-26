package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<Inventory, String> {
    boolean existsByProductId(String productId);
}

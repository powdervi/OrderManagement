package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Inventory;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, String> {
    boolean existsByProductId(String productId);

    Optional<Inventory> findByProductId(String productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select i
            from Inventory i
            where i.productId = :productId
            """)
    Optional<Inventory> findByProductIdForUpdate(@Param("productId") String productId);

    List<Inventory> findAllByProductIdIn(List<String> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select i
            from Inventory i
            where i.productId in :productIds
            """)
    List<Inventory> findAllByProductIdInForUpdate(@Param("productIds") List<String> productIds);

    List<Inventory> findByProductIdIn(List<String> productIds);
}

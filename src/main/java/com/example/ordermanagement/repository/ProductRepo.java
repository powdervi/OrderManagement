package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProductRepo extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    @Query("SELECT COALESCE(SUM(p.basePrice * i.quantityInStock), 0) " +
            "FROM Product p, Inventory i " +
            "WHERE p.id = i.productId AND p.status = 'ACTIVE'")
    BigDecimal calculateTotalInventoryValue();

    @Query("SELECT COUNT(p) " +
            "FROM Product p, Inventory i " +
            "WHERE p.id = i.productId AND p.status = 'ACTIVE' AND i.quantityInStock <= :threshold")
    long countLowStockProducts(@Param("threshold") int threshold);
}

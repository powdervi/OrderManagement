package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepo extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
}

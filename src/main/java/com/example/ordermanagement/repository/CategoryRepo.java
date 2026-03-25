package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, String> {
    List<Category> findAllByParentIdIsNull();

    List<Category> findAllByParentId(String id);
}

package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.CategoryCreateReq;
import com.example.ordermanagement.dto.request.CategoryUpdateReq;
import com.example.ordermanagement.entity.Category;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {
    Category createCate(@Valid CategoryCreateReq categoryCreateReq);

    List<Category> getRoot();

    List<Category> getChild(String id);

    Category updateCate(String id, @Valid CategoryUpdateReq categoryUpdateReq);
}

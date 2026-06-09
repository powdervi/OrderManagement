package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.CategoryCreateReq;
import com.example.ordermanagement.dto.request.CategoryUpdateReq;
import com.example.ordermanagement.dto.response.CategoryRes;
import com.example.ordermanagement.dto.response.CategoryTreeRes;
import com.example.ordermanagement.entity.Category;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {
    CategoryRes createCate(@Valid CategoryCreateReq categoryCreateReq);
    List<CategoryRes> getRoot();
    List<CategoryRes> getChild(String id);
    List<CategoryTreeRes> getCategoryTree();
    CategoryRes updateCate(String id, @Valid CategoryUpdateReq categoryUpdateReq);
}

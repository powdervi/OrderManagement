package com.example.ordermanagement.mapper;

import com.example.ordermanagement.dto.request.CategoryCreateReq;
import com.example.ordermanagement.dto.request.CategoryUpdateReq;
import com.example.ordermanagement.dto.response.CategoryRes;
import com.example.ordermanagement.entity.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryRes toRes(Category category);

    List<CategoryRes> toResList(List<Category> categories);

    Category toEntity(CategoryCreateReq req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategoryFromReq(CategoryUpdateReq req, @MappingTarget Category category);
}
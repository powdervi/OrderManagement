package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.CategoryCreateReq;
import com.example.ordermanagement.dto.request.CategoryUpdateReq;
import com.example.ordermanagement.dto.response.AddressRes;
import com.example.ordermanagement.dto.response.CategoryRes;
import com.example.ordermanagement.entity.Category;
import com.example.ordermanagement.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<BaseResponse<CategoryRes>> createCategory(@RequestBody @Valid CategoryCreateReq categoryCreateReq){
        Category category = categoryService.createCate(categoryCreateReq);
        CategoryRes categoryChildRes = modelMapper.map(category, CategoryRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(categoryChildRes));
    }

    @GetMapping("/root")
    public ResponseEntity<BaseResponse<List<CategoryRes>>> getRoot(){
        List<Category> categoryList = categoryService.getRoot();
        List<CategoryRes> categoryResList = modelMapper.map(categoryList, new TypeToken<List<CategoryRes>>(){}.getType());
        return ResponseEntity.ok(BaseResponse.ofSuccess(categoryResList));
    }

    @GetMapping("{id}/children")
    public ResponseEntity<BaseResponse<List<CategoryRes>>> getChild(@PathVariable String id){
        List<Category> categoryList = categoryService.getChild(id);
        List<CategoryRes> categoryResList = modelMapper.map(categoryList, new TypeToken<List<CategoryRes>>(){}.getType());
        return ResponseEntity.ok(BaseResponse.ofSuccess(categoryResList));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<CategoryRes>> updateCate(@PathVariable String id, @RequestBody @Valid CategoryUpdateReq categoryUpdateReq){
        Category category = categoryService.updateCate(id, categoryUpdateReq);
        CategoryRes categoryChildRes = modelMapper.map(category, CategoryRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(categoryChildRes));
    }

}

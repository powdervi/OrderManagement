package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.CategoryCreateReq;
import com.example.ordermanagement.dto.request.CategoryUpdateReq;
import com.example.ordermanagement.dto.response.CategoryRes;
import com.example.ordermanagement.dto.response.CategoryTreeRes;
import com.example.ordermanagement.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse<CategoryRes>> createCategory(@RequestBody @Valid CategoryCreateReq req) {
        return ResponseEntity.ok(BaseResponse.ofSuccess(categoryService.createCate(req)));
    }

    @GetMapping("/root")
    public ResponseEntity<BaseResponse<List<CategoryRes>>> getRoot() {
        return ResponseEntity.ok(BaseResponse.ofSuccess(categoryService.getRoot()));
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<BaseResponse<List<CategoryRes>>> getChild(@PathVariable String id) {
        return ResponseEntity.ok(BaseResponse.ofSuccess(categoryService.getChild(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<CategoryRes>> updateCate(
            @PathVariable String id,
            @RequestBody @Valid CategoryUpdateReq req) {
        return ResponseEntity.ok(BaseResponse.ofSuccess(categoryService.updateCate(id, req)));
    }
    @GetMapping("/tree-dropdown")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_STAFF')")
    public ResponseEntity<BaseResponse<List<CategoryTreeRes>>> getCategoryTree() {
        List<CategoryTreeRes> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(BaseResponse.ofSuccess(tree));
    }

}
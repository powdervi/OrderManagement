package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.ProductCreateReq;
import com.example.ordermanagement.dto.request.ProductSearchReq;
import com.example.ordermanagement.dto.request.ProductUpdateReq;
import com.example.ordermanagement.dto.response.InventorySummaryRes;
import com.example.ordermanagement.dto.response.ProductRes;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/inventory-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_STAFF')")
    public ResponseEntity<BaseResponse<InventorySummaryRes>> getInventorySummary() {
        InventorySummaryRes summary = productService.getInventorySummary();
        return ResponseEntity.ok(BaseResponse.ofSuccess(summary));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse<ProductRes>> createProduct(@RequestBody @Valid ProductCreateReq productCreateReq) {
        ProductRes productRes = productService.createProduct(productCreateReq);
        return ResponseEntity.ok(BaseResponse.ofSuccess(productRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductRes>> updateProduct(
            @PathVariable String id,
            @RequestBody @Valid ProductUpdateReq productUpdateReq
    ) {
        ProductRes productRes = productService.updateProduct(id, productUpdateReq);
        return ResponseEntity.ok(BaseResponse.ofSuccess(productRes));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_STAFF')")
    public ResponseEntity<BaseResponse<List<ProductRes>>> search(
            @RequestParam(name = "page_size", defaultValue = "20") Integer pageSize,
            @RequestParam(name = "page_number", defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false) String sort,
            ProductSearchReq req) {

        Page<ProductRes> result = productService.search(pageSize, pageNumber, sort, req);

        return ResponseEntity.ok(BaseResponse.ofSuccess(result));
    }

}

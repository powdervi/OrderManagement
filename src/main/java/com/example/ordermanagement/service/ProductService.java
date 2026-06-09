package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.ProductCreateReq;
import com.example.ordermanagement.dto.request.ProductSearchReq;
import com.example.ordermanagement.dto.request.ProductUpdateReq;
import com.example.ordermanagement.dto.response.InventorySummaryRes;
import com.example.ordermanagement.dto.response.ProductDetailRes;
import com.example.ordermanagement.dto.response.ProductRes;
import com.example.ordermanagement.entity.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    InventorySummaryRes getInventorySummary();

    ProductDetailRes getProductDetail(String id);

    ProductRes createProduct(@Valid ProductCreateReq productCreateReq);

    ProductRes updateProduct(String id, @Valid ProductUpdateReq productUpdateReq);

    Page<ProductRes> search(Integer pageSize, Integer pageNumber, String sort, ProductSearchReq productSearchReq);
}

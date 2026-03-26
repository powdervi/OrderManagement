package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.ProductCreateReq;
import com.example.ordermanagement.dto.request.ProductSearchReq;
import com.example.ordermanagement.dto.request.ProductUpdateReq;
import com.example.ordermanagement.entity.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product createProduct(@Valid ProductCreateReq productCreateReq);

    Product updateProduct(String id, @Valid ProductUpdateReq productUpdateReq);

    Page<Product> search(Integer pageSize, Integer pageNumber, String sort, ProductSearchReq productSearchReq);
}

package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.ProductCreateReq;
import com.example.ordermanagement.dto.request.ProductSearchReq;
import com.example.ordermanagement.dto.request.ProductUpdateReq;
import com.example.ordermanagement.dto.response.AddressRes;
import com.example.ordermanagement.dto.response.ProductRes;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<BaseResponse<ProductRes>> createProduct(@RequestBody @Valid ProductCreateReq productCreateReq) {
        Product product = productService.createProduct(productCreateReq);
        ProductRes productRes = modelMapper.map(product, ProductRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(productRes));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductRes>> updateProduct(
            @PathVariable String id,
            @RequestBody @Valid ProductUpdateReq productUpdateReq
    ) {
        Product product = productService.updateProduct(id, productUpdateReq);
        ProductRes productRes = modelMapper.map(product, ProductRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(productRes));
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<ProductRes>>> search(@RequestParam(name = "page_size", defaultValue = "20") Integer pageSize,
                                                                 @RequestParam(name = "page_number", defaultValue = "0") Integer pageNumber,
                                                                 @RequestParam(required = false) String sort,
                                                                 ProductSearchReq productSearchReq) {

        Page<Product> products = productService.search(pageSize,pageNumber,sort,productSearchReq);

        Page<ProductRes> result = modelMapper.map(products, new TypeToken<Page<ProductRes>>(){}.getType());

        return ResponseEntity.ok(BaseResponse.ofSuccess(result));
    }

}

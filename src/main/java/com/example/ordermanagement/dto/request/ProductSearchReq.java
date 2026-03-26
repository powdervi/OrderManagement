package com.example.ordermanagement.dto.request;

import com.example.ordermanagement.common.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchReq {
    private String name;
    private String categoryId;
    private ProductStatus status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}

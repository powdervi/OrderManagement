package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.common.ProductStatus;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDetailRes {
    private String id;
    private String sku;
    private String name;
    private String categoryId;
    private String categoryName;
    private BigDecimal basePrice;
    private BigDecimal weight;
    private String description;
    private Integer quantityInStock;
    private ProductStatus status;
}
package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.common.ProductStatus;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRes {
    private String id;
    private String sku;
    private String name;
    private String categoryName;
    private BigDecimal basePrice;
    private Integer quantityInStock;
    private ProductStatus status;
}
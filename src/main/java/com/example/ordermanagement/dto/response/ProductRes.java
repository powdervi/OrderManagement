package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.common.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductRes {

    private String id;
    private String categoryId;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private BigDecimal weight;
    private ProductStatus status;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
}

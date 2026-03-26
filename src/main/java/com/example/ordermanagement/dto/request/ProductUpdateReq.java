package com.example.ordermanagement.dto.request;

import com.example.ordermanagement.common.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductUpdateReq {

    private String categoryId;

    @Size(max = 255)
    private String name;

    private String description;

    @DecimalMin(value = "0", message = "Base price must be >= 0")
    private BigDecimal basePrice;

    @DecimalMin(value = "0", message = "Weight must be >= 0")
    private BigDecimal weight;

    private ProductStatus status;
}

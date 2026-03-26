package com.example.ordermanagement.dto.request;

import com.example.ordermanagement.common.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductCreateReq {

    @NotBlank(message = "CategoryId must not be blank")
    private String categoryId;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull(message = "Base price must not be null")
    @DecimalMin(value = "0", message = "Base price must be >= 0")
    private BigDecimal basePrice;

    @DecimalMin(value = "0", message = "Weight must be >= 0")
    private BigDecimal weight;

    private ProductStatus status;
}

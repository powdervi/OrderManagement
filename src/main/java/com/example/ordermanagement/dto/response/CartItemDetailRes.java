package com.example.ordermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDetailRes {
    private String cartItemId;
    private String productId;
    private String productName;
    private String description;
    private BigDecimal basePrice;
    private Integer quantity;
    private BigDecimal lineTotal;
}

package com.example.ordermanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryCreateReq {

    @NotNull(message = "Quantity in stock must not be null")
    @Min(value = 0, message = "Quantity in stock must be >= 0")
    private Integer quantityInStock;
}

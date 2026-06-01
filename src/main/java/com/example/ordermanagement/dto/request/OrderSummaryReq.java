package com.example.ordermanagement.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderSummaryReq {

    @NotEmpty(message = "Items must not be empty")
    @Valid
    private List<OrderItemReq> items;

    private String promotionId;
}

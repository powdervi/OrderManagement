package com.example.ordermanagement.dto.request;

import com.example.ordermanagement.common.OrderSource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlaceOrderReq {

    @NotEmpty(message = "Items must not be empty")
    @Valid
    private List<OrderItemReq> items;

    @NotBlank(message = "Address id is required")
    private String addressId;

    @NotBlank(message = "Payment method id is required")
    private String paymentMethodId;

    @NotBlank(message = "Carrier id is required")
    private String carrierId;

    private String promotionId;

    private String note;

    @NotNull(message = "Order source is required")
    private OrderSource source;
}

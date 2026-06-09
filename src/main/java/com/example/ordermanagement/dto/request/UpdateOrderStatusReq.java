package com.example.ordermanagement.dto.request;

import com.example.ordermanagement.common.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusReq {

    @NotNull(message = "Status is required")
    private OrderStatus status;

    @Size(max = 500)
    private String note;

    @Size(max = 255)
    private String location;
}

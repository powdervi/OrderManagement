package com.example.ordermanagement.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BulkConfirmOrderReq {

    @NotEmpty(message = "List of order IDs cannot be empty")
    private List<String> orderIds;

}

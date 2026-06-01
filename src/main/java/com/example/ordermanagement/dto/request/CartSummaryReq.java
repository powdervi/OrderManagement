package com.example.ordermanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartSummaryReq {

    private List<String> cartItemIds;

    private String promotionId;
}

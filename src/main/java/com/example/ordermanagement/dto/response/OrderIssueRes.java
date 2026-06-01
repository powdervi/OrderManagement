package com.example.ordermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderIssueRes {

    private String productId;

    private String type;

    private String message;
}

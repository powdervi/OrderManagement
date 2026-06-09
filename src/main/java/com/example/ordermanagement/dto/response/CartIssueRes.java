package com.example.ordermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartIssueRes {
    private String cartItemId;
    private String type;
    private String message;
}

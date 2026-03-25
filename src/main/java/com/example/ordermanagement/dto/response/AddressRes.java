package com.example.ordermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddressRes {
    private String id;
    private String userId;
    private String recipientName;
    private String recipientPhone;
    private String province;
    private String district;
    private String ward;
    private String street;
    private String detail;
    private String postalCode;
    private Boolean isDefault;
}
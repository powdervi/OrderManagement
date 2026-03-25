package com.example.ordermanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressUpdateReq {

    @Size(max = 255)
    private String recipientName;

    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$",
            message = "Invalid phone number format")
    private String recipientPhone;

    @Size(max = 100)
    private String province;

    @Size(max = 100)
    private String district;

    @Size(max = 100)
    private String ward;

    private String street;

    @Size(max = 500)
    private String detail;

    private String postalCode;

    private Boolean isDefault;
}

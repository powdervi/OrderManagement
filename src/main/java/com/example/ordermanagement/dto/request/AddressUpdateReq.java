package com.example.ordermanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressUpdateReq {

    @Size(min = 1, max = 255, message = "Recipient name must be between 1 and 255 characters")
    private String recipientName;

    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message = "Invalid phone number format")
    private String recipientPhone;

    @Size(min = 1, max = 100)
    private String province;

    @Size(min = 1, max = 100)
    private String district;

    @Size(min = 1, max = 100)
    private String ward;

    @Size(max = 255)
    private String street;

    @Size(min = 1, max = 500)
    private String detail;

    @Size(max = 20)
    private String postalCode;

    private Boolean isDefault;
}

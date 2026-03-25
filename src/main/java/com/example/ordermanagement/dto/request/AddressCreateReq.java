package com.example.ordermanagement.dto.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddressCreateReq implements Serializable {

    @NotBlank(message = "Recipient name must not be blank")
    @Size(max = 255)
    private String recipientName;

    @NotBlank(message = "Recipient phone must not be blank")
    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$",
            message = "Invalid phone number format")
    private String recipientPhone;

    @NotBlank(message = "Province must not be blank")
    @Size(max = 100)
    private String province;

    @NotBlank(message = "District must not be blank")
    @Size(max = 100)
    private String district;

    @NotBlank(message = "Ward must not be blank")
    @Size(max = 100)
    private String ward;

    private String street;

    @NotBlank(message = "Detail must not be blank")
    @Size(max = 500)
    private String detail;

    private String postalCode;

    private Boolean isDefault;
}

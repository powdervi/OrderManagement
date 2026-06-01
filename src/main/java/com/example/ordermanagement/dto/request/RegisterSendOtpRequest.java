package com.example.ordermanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSendOtpRequest {

    @NotBlank(message = "username must not be blank")
    @Size(min = 3, max = 20, message = "user must beetwen 3 to 20 char")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "username must be alphabet")
    private String username;

    @NotBlank(message = "firstName must not be blank")
    @Size(min = 3, max = 20, message = "firstName must beetwen 3 to 20 char")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "username must be alphabet")
    private String firstName;

    @NotBlank(message = "lastName must not be blank")
    @Size(min = 3, max = 20, message = "lastName must beetwen 3 to 20 char")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "username must be alphabet")
    private String lastName;

    @NotBlank(message = "email must not be blank")
    @Email
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$",
            message = "Invalid phone number format")
    @NotBlank(message = "phone must not be blank")
    private String phone;

    @NotBlank(message = "passWord must not be blank")
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Password must contain upper, lower, number and special character")
    private String password;
}

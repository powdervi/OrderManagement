package com.example.ordermanagement.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateReq {
    @Size(min = 3, max = 20, message = "user must beetwen 3 to 20 char")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "username must be alphabet")
    private String username;

    @Size(min = 3, max = 20, message = "firstName must beetwen 3 to 20 char")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "username must be alphabet")
    private String firstName;

    @Size(min = 3, max = 20, message = "lastName must beetwen 3 to 20 char")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "username must be alphabet")
    private String lastName;

    @Email
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$",
            message = "Invalid phone number format")
    private String phone;
}

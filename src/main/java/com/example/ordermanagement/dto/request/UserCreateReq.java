package com.example.ordermanagement.dto.request;

import com.example.ordermanagement.common.UserRole;
import com.example.ordermanagement.common.UserStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateReq implements Serializable {

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
    private String passwordHash;

    @NotNull(message = "role must not be blank")
    private UserRole role;

    @NotNull(message = "status must not be blank")
    private UserStatus status;
}

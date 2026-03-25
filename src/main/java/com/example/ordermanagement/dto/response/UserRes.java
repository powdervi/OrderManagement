package com.example.ordermanagement.dto.response;

import com.example.ordermanagement.common.UserRole;
import com.example.ordermanagement.common.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {
    private String id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private UserRole role;

    private UserStatus status;

}

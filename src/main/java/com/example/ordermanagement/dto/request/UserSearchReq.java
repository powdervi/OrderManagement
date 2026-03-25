package com.example.ordermanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UserSearchReq {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private String status;
}

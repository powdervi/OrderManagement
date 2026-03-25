package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.UserCreateReq;
import com.example.ordermanagement.dto.request.UserSearchReq;
import com.example.ordermanagement.dto.request.UserUpdateReq;
import com.example.ordermanagement.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    User createUser(@Valid UserCreateReq userCreateReq);

    User updateUser(String id, UserUpdateReq userUpdateReq);

    void deleteUser(String id);

    User getById(String id);

    List<User> search(UserSearchReq userSearchReq);
}

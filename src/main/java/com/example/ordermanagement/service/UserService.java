package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.UserCreateReq;
import com.example.ordermanagement.dto.request.UserSearchReq;
import com.example.ordermanagement.dto.request.UserUpdateReq;
import com.example.ordermanagement.dto.response.UserRes;
import com.example.ordermanagement.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserRes createUser(UserCreateReq userCreateReq);

    UserRes updateUser(String id, UserUpdateReq userUpdateReq);

    void deleteUser(String id);

    UserRes getById(String id);

    Page<UserRes> search(UserSearchReq userSearchReq, Pageable pageable);
}

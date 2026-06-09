package com.example.ordermanagement.mapper;

import com.example.ordermanagement.dto.request.UserCreateReq;
import com.example.ordermanagement.dto.request.UserUpdateReq;
import com.example.ordermanagement.dto.response.UserRes;
import com.example.ordermanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toEntity(UserCreateReq req);

    UserRes toRes(User user);

    List<UserRes> toResList(List<User> users);

    void updateUserFromReq(UserUpdateReq req, @MappingTarget User user);
}

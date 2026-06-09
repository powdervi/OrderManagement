package com.example.ordermanagement.mapper;

import com.example.ordermanagement.dto.response.CartRes;
import com.example.ordermanagement.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartRes  toCartRes(Cart cart);
}

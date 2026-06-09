package com.example.ordermanagement.mapper;

import com.example.ordermanagement.dto.response.CartItemDetailRes;
import com.example.ordermanagement.dto.response.CartItemRes;
import com.example.ordermanagement.entity.CartItem;
import com.example.ordermanagement.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemRes toCartItemRes(CartItem cartItem);

    @Mapping(source = "cartItem.id", target = "cartItemId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.description", target = "description")
    @Mapping(source = "product.basePrice", target = "basePrice")
    @Mapping(source = "cartItem.quantity", target = "quantity")
    @Mapping(target = "lineTotal", expression = "java(product.getBasePrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity())))")
    CartItemDetailRes toCartItemDetailRes(CartItem cartItem, Product product);
}

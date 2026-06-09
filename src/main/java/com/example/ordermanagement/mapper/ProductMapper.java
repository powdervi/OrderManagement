package com.example.ordermanagement.mapper;

import com.example.ordermanagement.dto.request.ProductCreateReq;
import com.example.ordermanagement.dto.request.ProductUpdateReq;
import com.example.ordermanagement.dto.response.ProductDetailRes;
import com.example.ordermanagement.dto.response.ProductRes;
import com.example.ordermanagement.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "quantityInStock", ignore = true)
    ProductRes toProductRes(Product product);

    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "quantityInStock", ignore = true)
    ProductDetailRes toProductDetailRes(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromReq(ProductUpdateReq req, @MappingTarget Product product);

    Product toEntity(ProductCreateReq req);

}
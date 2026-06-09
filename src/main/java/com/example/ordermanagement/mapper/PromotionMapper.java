package com.example.ordermanagement.mapper;

import com.example.ordermanagement.dto.response.PromotionRes;
import com.example.ordermanagement.entity.Promotion;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    PromotionRes toPromotionRes(Promotion promotion);

    List<PromotionRes> toPromotionResList(List<Promotion> promotions);
}

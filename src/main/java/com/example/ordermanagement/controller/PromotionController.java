package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.PromotionSearchRequest;
import com.example.ordermanagement.dto.response.PromotionRes;
import com.example.ordermanagement.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.example.ordermanagement.entity.Promotion;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    private final ModelMapper modelMapper;

    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @GetMapping()
    public ResponseEntity<BaseResponse<List<PromotionRes>>> search(@RequestParam(name = "page_size", defaultValue = "20") Integer pageSize,
                                                                   @RequestParam(name = "page_number", defaultValue = "0") Integer pageNumber,
                                                                   @RequestParam(required = false) String sort,
                                                                   PromotionSearchRequest promotionSearchReq) {

        Page<Promotion> promotions = promotionService.search(pageSize, pageNumber, sort, promotionSearchReq);
        Page<PromotionRes> result = modelMapper.map(promotions, new TypeToken<Page<PromotionRes>>() {
        }.getType());
        return ResponseEntity.ok(BaseResponse.ofSuccess(result));
    }

    //todo 1: Giỏ hàng & Kiểm tra tồn kho: api list promotion
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @GetMapping("/available")
    public ResponseEntity<BaseResponse<List<PromotionRes>>> getAvailablePromotions() {

        List<Promotion> promotions = promotionService.getAvailablePromotions();
        List<PromotionRes> result =  modelMapper.map(promotions, new TypeToken<Page<PromotionRes>>() {
        }.getType());
        return ResponseEntity.ok(BaseResponse.ofSuccess(result));
    }
}
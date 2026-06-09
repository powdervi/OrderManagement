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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @GetMapping()
    public ResponseEntity<BaseResponse<List<PromotionRes>>> search(@RequestParam(name = "page_size", defaultValue = "20") Integer pageSize,
                                                                   @RequestParam(name = "page_number", defaultValue = "0") Integer pageNumber,
                                                                   @RequestParam(required = false) String sort,
                                                                   PromotionSearchRequest promotionSearchReq) {

        Page<PromotionRes> promotions = promotionService.search(pageSize, pageNumber, sort, promotionSearchReq);
        return ResponseEntity.ok(BaseResponse.ofSuccess(promotions));
    }

    //todo 1: Giỏ hàng & Kiểm tra tồn kho: api list promotion
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @GetMapping("/available")
    public ResponseEntity<BaseResponse<List<PromotionRes>>> getAvailablePromotions(
            @RequestParam(name = "page_size", defaultValue = "20") Integer pageSize,
            @RequestParam(name = "page_number", defaultValue = "0") Integer pageNumber) {

        Page<PromotionRes> promotions = promotionService.getAvailablePromotions(pageSize, pageNumber);
        return ResponseEntity.ok(BaseResponse.ofSuccess(promotions));
    }
}
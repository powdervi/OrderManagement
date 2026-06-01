package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.PromotionSearchRequest;
import com.example.ordermanagement.entity.Promotion;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PromotionService {

    Page<Promotion> search(Integer pageSize, Integer pageNumber, String sort, PromotionSearchRequest req);

    List<Promotion> getAvailablePromotions();
}

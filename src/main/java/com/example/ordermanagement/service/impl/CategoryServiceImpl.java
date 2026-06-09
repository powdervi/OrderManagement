package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.dto.request.CategoryCreateReq;
import com.example.ordermanagement.dto.request.CategoryUpdateReq;
import com.example.ordermanagement.dto.response.CategoryRes;
import com.example.ordermanagement.dto.response.CategoryTreeRes;
import com.example.ordermanagement.entity.Category;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.mapper.CategoryMapper;
import com.example.ordermanagement.repository.CategoryRepo;
import com.example.ordermanagement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryRes createCate(CategoryCreateReq req) {
        Category category = categoryMapper.toEntity(req);

        if (req.getParentId() != null && !req.getParentId().isBlank()) {
            getCategoryEntity(req.getParentId());
        } else {
            category.setParentId(null);
        }

        return categoryMapper.toRes(categoryRepo.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryRes> getRoot() {
        return categoryMapper.toResList(categoryRepo.findAllByParentIdIsNull());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryRes> getChild(String id) {
        return categoryMapper.toResList(categoryRepo.findAllByParentId(id));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryRes updateCate(String id, CategoryUpdateReq req) {
        Category category = getCategoryEntity(id);

        if (req.getParentId() != null && !req.getParentId().isBlank()) {
            if (req.getParentId().equals(id)) {
                throw new MHException(MHErrors.CATEGORY_DUPLICATED);
            }

            List<Category> allCategories = categoryRepo.findAll();

            Map<String, String> categoryTreeMap = new HashMap<>();
            for (Category c : allCategories) {
                categoryTreeMap.put(c.getId(), c.getParentId());
            }

            if (!categoryTreeMap.containsKey(req.getParentId())) {
                throw new MHException(MHErrors.CATEGORY_NOT_FOUND);
            }

            if (!validateParentInMemory(req.getParentId(), id, categoryTreeMap)) {
                throw new MHException(MHErrors.CATEGORY_LOOP);
            }
        }

        categoryMapper.updateCategoryFromReq(req, category);

        if ("".equals(req.getParentId())) {
            category.setParentId(null);
        }

        return categoryMapper.toRes(categoryRepo.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTreeRes> getCategoryTree() {

        List<Category> allCategories = categoryRepo.findAll();

        List<CategoryTreeRes> allNodes = allCategories.stream()
                .map(c -> new CategoryTreeRes(c.getId(), c.getName(), c.getParentId()))
                .toList();

        Map<String, List<CategoryTreeRes>> childrenMap = allNodes.stream()
                .filter(node -> node.getParentId() != null)
                .collect(Collectors.groupingBy(CategoryTreeRes::getParentId));

        allNodes.forEach(node -> {
            List<CategoryTreeRes> children = childrenMap.getOrDefault(node.getId(), new ArrayList<>());
            node.setChildren(children);
        });

        return allNodes.stream()
                .filter(node -> node.getParentId() == null)
                .toList();
    }

    private boolean validateParentInMemory(String newParentId, String currentCategoryId, Map<String, String> categoryTreeMap) {
        String currentPointer = newParentId;

        while (currentPointer != null) {
            if (currentPointer.equals(currentCategoryId)) {
                return false;
            }
            currentPointer = categoryTreeMap.get(currentPointer);
        }

        return true;
    }

    private Category getCategoryEntity(String id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new MHException(MHErrors.CATEGORY_NOT_FOUND));
    }
}
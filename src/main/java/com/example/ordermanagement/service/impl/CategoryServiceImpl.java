package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.dto.request.CategoryCreateReq;
import com.example.ordermanagement.dto.request.CategoryUpdateReq;
import com.example.ordermanagement.entity.Category;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.CategoryRepo;
import com.example.ordermanagement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Category createCate(CategoryCreateReq categoryCreateReq) {
        Category category = new Category();
        category.setName(categoryCreateReq.getName());
        category.setDescription(categoryCreateReq.getDescription());

        if (categoryCreateReq.getParentId() != null && !categoryCreateReq.getParentId().isBlank()) {
            Category parent = getCategoryEntity(categoryCreateReq.getParentId());
            category.setParentId(parent.getId());
        } else {
            category.setParentId(null);
        }

        return categoryRepo.save(category);
    }

    @Override
    public List<Category> getRoot() {
        return categoryRepo.findAllByParentIdIsNull();
    }

    @Override
    public List<Category> getChild(String id) {
        return categoryRepo.findAllByParentId(id);
    }

    @Override
    @Transactional
    public Category updateCate(String id, CategoryUpdateReq categoryUpdateReq) {
        Category category = getCategoryEntity(id);

        if(categoryUpdateReq.getName() != null && !categoryUpdateReq.getName().isBlank()){
            category.setName(categoryUpdateReq.getName());
        }

        if(categoryUpdateReq.getDescription() != null && !categoryUpdateReq.getDescription().isBlank()){
            category.setDescription(categoryUpdateReq.getDescription());
        }

        if(categoryUpdateReq.getParentId() != null && !categoryUpdateReq.getParentId().isBlank()){
            if (categoryUpdateReq.getParentId().equals(id)) {
                throw new MHException(MHErrors.CATEGORY_DUPLICATED);
            }

            Category parent = getCategoryEntity(categoryUpdateReq.getParentId());
            if(!validateParent(parent, id)){
                throw new MHException(MHErrors.CATEGORY_LOOP);
            }

            category.setParentId(parent.getId());
        }

        return categoryRepo.save(category);
    }

    private boolean validateParent(Category parent, String id) {
        if (parent.getId() != null && parent.getId().equals(id)) {
            return false;
        }
        if(parent.getParentId() == null){
            return true;
        }else{
            Category grand = getCategoryEntity(parent.getParentId());
            return validateParent(grand, id);
        }
    }

    private Category getCategoryEntity(String parentId) {
        return categoryRepo.findById(parentId)
                .orElseThrow(() -> new MHException(MHErrors.CATEGORY_NOT_FOUND));
    }
}

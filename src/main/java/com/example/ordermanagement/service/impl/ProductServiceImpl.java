package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.dto.request.ProductCreateReq;
import com.example.ordermanagement.dto.request.ProductSearchReq;
import com.example.ordermanagement.dto.request.ProductUpdateReq;
import com.example.ordermanagement.entity.Category;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.CategoryRepo;
import com.example.ordermanagement.repository.ProductRepo;
import com.example.ordermanagement.service.ProductService;
import com.example.ordermanagement.service.spec.ProductSpecification;
import com.example.ordermanagement.utils.SortUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final CategoryRepo categoryRepo;

    @Override
    @Transactional
    public Product createProduct(ProductCreateReq productCreateReq) {
        Product product = modelMapper.map(productCreateReq, Product.class);
        product.setId(null);

        validateCate(productCreateReq.getCategoryId());

        log.info("Creating product with id {} and name {}", product.getId(), product.getName());
        return productRepo.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(String id, ProductUpdateReq productUpdateReq) {
        Product product = getProductEntity(id);

        if (productUpdateReq.getCategoryId() != null && !productUpdateReq.getCategoryId().isBlank()) {
            validateCate(productUpdateReq.getCategoryId());

            product.setCategoryId(productUpdateReq.getCategoryId());
        }

        if (productUpdateReq.getName() != null && !productUpdateReq.getName().isBlank()) {
            product.setName(productUpdateReq.getName());
        }

        if (productUpdateReq.getDescription() != null) {
            product.setDescription(productUpdateReq.getDescription());
        }

        if (productUpdateReq.getBasePrice() != null) {
            product.setBasePrice(productUpdateReq.getBasePrice());
        }

        if (productUpdateReq.getWeight() != null) {
            product.setWeight(productUpdateReq.getWeight());
        }

        if (productUpdateReq.getStatus() != null) {
            product.setStatus(productUpdateReq.getStatus());
        }

        return productRepo.save(product);
    }

    @Override
    public Page<Product> search(Integer pageSize, Integer pageNumber, String sort, ProductSearchReq productSearchReq) {
        Specification<Product> specification = Specification.unrestricted();

        if (productSearchReq.getName() != null && !productSearchReq.getName().isBlank()) {
            specification = specification.and(ProductSpecification.likeName(productSearchReq.getName()));
        }

        if (productSearchReq.getCategoryId() != null && !productSearchReq.getCategoryId().isBlank()) {
            specification = specification.and(ProductSpecification.equalCategoryId(productSearchReq.getCategoryId()));
        }

        if (productSearchReq.getStatus() != null) {
            specification = specification.and(ProductSpecification.equalStatus(productSearchReq.getStatus()));
        }

        if (productSearchReq.getMinPrice() != null) {
            specification = specification.and(ProductSpecification.greaterThanOrEqualMinPrice(productSearchReq.getMinPrice()));
        }

        if (productSearchReq.getMaxPrice() != null) {
            specification = specification.and(ProductSpecification.lessThanOrEqualMaxPrice(productSearchReq.getMaxPrice()));
        }


        Sort sortObj = SortUtil.buildSort(sort);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortObj);

        return productRepo.findAll(specification, pageable);
    }

    private void validateCate(String categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new MHException(MHErrors.CATEGORY_NOT_FOUND));

        if (!categoryRepo.findAllByParentId(category.getId()).isEmpty()) {
            throw new MHException(MHErrors.CATEGORY_IS_NOT_LEAF);
        }
    }

    private Product getProductEntity(String id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new MHException(MHErrors.PRODUCT_NOT_FOUND));
    }
}

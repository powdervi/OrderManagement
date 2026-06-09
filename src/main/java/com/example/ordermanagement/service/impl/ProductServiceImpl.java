package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.ProductStatus;
import com.example.ordermanagement.dto.request.ProductCreateReq;
import com.example.ordermanagement.dto.request.ProductSearchReq;
import com.example.ordermanagement.dto.request.ProductUpdateReq;
import com.example.ordermanagement.dto.response.InventorySummaryRes;
import com.example.ordermanagement.dto.response.ProductDetailRes;
import com.example.ordermanagement.dto.response.ProductRes;
import com.example.ordermanagement.entity.Category;
import com.example.ordermanagement.entity.Inventory;
import com.example.ordermanagement.entity.Product;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.mapper.ProductMapper;
import com.example.ordermanagement.repository.CategoryRepo;
import com.example.ordermanagement.repository.InventoryRepo;
import com.example.ordermanagement.repository.ProductRepo;
import com.example.ordermanagement.service.ProductService;
import com.example.ordermanagement.service.helper.OrderCodeGenerator;
import com.example.ordermanagement.service.spec.ProductSpecification;
import com.example.ordermanagement.utils.SortUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final InventoryRepo inventoryRepo;
    private final ProductMapper productMapper;
    private final OrderCodeGenerator  orderCodeGenerator;


    @Override
    @Transactional(readOnly = true)
    public InventorySummaryRes getInventorySummary() {
        BigDecimal totalValue = productRepo.calculateTotalInventoryValue();

        long totalProducts = productRepo.count();

        int lowStockThreshold = 10;
        long lowStockCount = productRepo.countLowStockProducts(lowStockThreshold);

        return InventorySummaryRes.builder()
                .totalInventoryValue(totalValue)
                .totalProducts(totalProducts)
                .lowStockCount(lowStockCount)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailRes getProductDetail(String id) {
        Product product = getProductEntity(id);
        ProductDetailRes res = productMapper.toProductDetailRes(product);

        inventoryRepo.findByProductId(product.getId())
                .ifPresent(inv -> res.setQuantityInStock(inv.getQuantityInStock()));

        if (product.getCategoryId() != null) {
            categoryRepo.findById(product.getCategoryId())
                    .ifPresent(cat -> res.setCategoryName(cat.getName()));
        }

        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductRes createProduct(ProductCreateReq req) {

        validateCate(req.getCategoryId());

        Product product = productMapper.toEntity(req);

        if (req.getSku() == null || req.getSku().isBlank()) {
            product.setSku(orderCodeGenerator.generateProductSku());
            log.info("System auto-generated SKU: {}", product.getSku());
        } else {
            product.setSku(req.getSku().trim().toUpperCase());
            log.info("Using manual SKU: {}", product.getSku());
        }

        product.setStatus(ProductStatus.ACTIVE);

        product = productRepo.save(product);

        Inventory inventory = new Inventory();
        inventory.setProductId(product.getId());
        inventory.setQuantityInStock(req.getQuantityInStock() != null ? req.getQuantityInStock() : 0);
        inventoryRepo.save(inventory);

        ProductRes res = productMapper.toProductRes(product);
        res.setQuantityInStock(inventory.getQuantityInStock());

        categoryRepo.findById(product.getCategoryId())
                .ifPresent(cat -> res.setCategoryName(cat.getName()));

        return res;
    }

    private void validateCate(String categoryId) {
        if (categoryId == null || categoryId.isBlank()) return;

        if (!categoryRepo.existsById(categoryId)) {
            throw new MHException(MHErrors.CATEGORY_NOT_FOUND);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductRes updateProduct(String id, ProductUpdateReq req) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new MHException(MHErrors.PRODUCT_NOT_FOUND));

        if (req.getCategoryId() != null && !req.getCategoryId().isBlank()) {
            if (!categoryRepo.existsById(req.getCategoryId())) {
                throw new MHException(MHErrors.CATEGORY_NOT_FOUND);
            }
        }

        if (req.getSku() != null && !req.getSku().isBlank()) {
            req.setSku(req.getSku().trim().toUpperCase());
        }

        productMapper.updateProductFromReq(req, product);

        product = productRepo.save(product);

        // 5. Cập nhật số lượng Tồn kho
        int currentStock = 0;
        if (req.getQuantityInStock() != null) {
            Inventory inventory = inventoryRepo.findByProductId(id)
                    .orElseGet(() -> {
                        Inventory newInv = new Inventory();
                        newInv.setProductId(id);
                        return newInv;
                    });

            inventory.setQuantityInStock(req.getQuantityInStock());
            inventoryRepo.save(inventory);
            currentStock = req.getQuantityInStock();
        } else {
            currentStock = inventoryRepo.findByProductId(product.getId())
                    .map(Inventory::getQuantityInStock).orElse(0);
        }

        ProductRes res = productMapper.toProductRes(product);
        res.setQuantityInStock(currentStock);

        if (product.getCategoryId() != null) {
            categoryRepo.findById(product.getCategoryId())
                    .ifPresent(cat -> res.setCategoryName(cat.getName()));
        }

        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductRes> search(Integer pageSize, Integer pageNumber, String sort, ProductSearchReq req) {

        Specification<Product> spec = Specification.unrestricted();

        if (req != null) {
            if (req.getKeyword() != null && !req.getKeyword().isBlank()) {
                spec = spec.and(ProductSpecification.likeKeyword(req.getKeyword()));
            }
            if (req.getCategoryId() != null && !req.getCategoryId().isBlank()) {
                spec = spec.and(ProductSpecification.equalCategoryId(req.getCategoryId()));
            }
            if (req.getStatus() != null) {
                spec = spec.and(ProductSpecification.equalStatus(req.getStatus()));
            }
            if (req.getMinPrice() != null) {
                spec = spec.and(ProductSpecification.greaterThanOrEqualMinPrice(req.getMinPrice()));
            }

            if (req.getMaxPrice() != null) {
                spec = spec.and(ProductSpecification.lessThanOrEqualMaxPrice(req.getMaxPrice()));
            }
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, SortUtil.buildSort(sort != null ? sort : "-createdAt"));
        Page<Product> productPage = productRepo.findAll(spec, pageable);

        if (productPage.isEmpty()) {
            return Page.empty(pageable);
        }

        List<String> productIds = productPage.getContent().stream()
                .map(Product::getId)
                .toList();

        Set<String> categoryIds = productPage.getContent().stream()
                .map(Product::getCategoryId)
                .filter(id -> id != null && !id.isBlank())
                .collect(Collectors.toSet());

        Map<String, Integer> inventoryMap = inventoryRepo.findByProductIdIn(productIds).stream()
                .collect(Collectors.toMap(Inventory::getProductId, Inventory::getQuantityInStock));

        Map<String, String> categoryMap = categoryRepo.findAllById(categoryIds).stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        return productPage.map(product -> {
            ProductRes res = productMapper.toProductRes(product);

            res.setQuantityInStock(inventoryMap.getOrDefault(product.getId(), 0));
            res.setCategoryName(categoryMap.getOrDefault(product.getCategoryId(), "Uncategorized"));

            return res;
        });
    }

    private Product getProductEntity(String id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new MHException(MHErrors.PRODUCT_NOT_FOUND));
    }
}

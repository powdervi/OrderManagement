package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.common.CarrierStatus;
import com.example.ordermanagement.common.DiscountType;
import com.example.ordermanagement.common.ProductStatus;
import com.example.ordermanagement.dto.request.CartSummaryReq;
import com.example.ordermanagement.dto.response.CarrierRes;
import com.example.ordermanagement.dto.response.CartIssueRes;
import com.example.ordermanagement.dto.response.CartSummaryRes;
import com.example.ordermanagement.entity.*;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.*;
import com.example.ordermanagement.service.CartService;
import com.example.ordermanagement.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;
    private final PromotionRepo promotionRepo;
    private final InventoryRepo inventoryRepo;
    private final ProductRepo productRepo;
    private final CarrierRepo carrierRepo;

    @Override
    @Transactional
    public Cart create(String userId) {
        if (!userRepo.existsById(userId)) {
            throw new MHException(MHErrors.USER_NOT_FOUND);
        }

        if (cartRepo.existsByUserId(userId)) {
            throw new MHException(MHErrors.CART_ALREADY_EXISTS);
        }

        Cart cart = new Cart();
        cart.setUserId(userId);

        return cartRepo.save(cart);
    }

    @Override
    public Cart getByUserId(String userId) {
        if (!userRepo.existsById(userId)) {
            throw new MHException(MHErrors.USER_NOT_FOUND);
        }

        return cartRepo.findByUserId(userId).orElseThrow(() -> new MHException(MHErrors.CART_NOT_FOUND));
    }

    @Override
    public CartSummaryRes getSummary(CartSummaryReq req) {

        validateSummaryRequest(req);

        String userId = SecurityUtils.getCurrentUserId();

        Cart cart = cartRepo.findByUserId(userId).orElseThrow(() -> new MHException(MHErrors.CART_NOT_FOUND));

        List<CartItem> cartItems = cartItemRepo.findAllByIdInAndCartId(req.getCartItemIds(), cart.getId());

        validateCartItems(req.getCartItemIds(), cartItems);

        List<CartIssueRes> issues = new ArrayList<>();

        BigDecimal subtotal = calculateSubtotal(cartItems, issues);

        BigDecimal discount = calculateDiscount(subtotal, req.getPromotionId(), issues);

        Carrier carrier = randomCarrier();

        BigDecimal shippingFee = carrier.getShippingFee();

        BigDecimal grandTotal = subtotal.subtract(discount).add(shippingFee);

        CartSummaryRes res = new CartSummaryRes();

        res.setSubtotal(subtotal);

        res.setDiscount(discount);

        res.setShippingFee(shippingFee);

        res.setGrandTotal(grandTotal);

        res.setIssues(issues);

        res.setValid(issues.isEmpty());

        CarrierRes carrierRes = new CarrierRes();

        carrierRes.setId(carrier.getId());

        carrierRes.setName(carrier.getName());

        carrierRes.setPhone(carrier.getPhone());

        carrierRes.setShippingFee(carrier.getShippingFee());

        res.setCarrier(carrierRes);

        return res;
    }


    private void validateSummaryRequest(CartSummaryReq req) {

        if (req.getCartItemIds() == null || req.getCartItemIds().isEmpty()) {

            throw new MHException(MHErrors.CART_ITEM_REQUIRED);
        }

        Set<String> uniqueIds = new HashSet<>(req.getCartItemIds());

        if (uniqueIds.size() != req.getCartItemIds().size()) {

            throw new MHException(MHErrors.DUPLICATE_CART_ITEM);
        }
    }

    private void validateCartItems(List<String> requestIds, List<CartItem> cartItems) {

        if (requestIds.size() != cartItems.size()) {

            throw new MHException(MHErrors.CART_ITEM_NOT_FOUND);
        }
    }

    private BigDecimal calculateSubtotal(List<CartItem> cartItems, List<CartIssueRes> issues) {

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {

            Product product = productRepo.findById(cartItem.getProductId()).orElseThrow(() -> new MHException(MHErrors.PRODUCT_NOT_FOUND));

            if (product.getStatus() != ProductStatus.ACTIVE) {

                CartIssueRes issue = new CartIssueRes();

                issue.setCartItemId(cartItem.getId());

                issue.setType("PRODUCT_INACTIVE");

                issue.setMessage("Product is inactive");

                issues.add(issue);

                continue;

            }

            Inventory inventory = inventoryRepo.findByProductId(product.getId()).orElseThrow(() -> new MHException(MHErrors.INVENTORY_NOT_FOUND));

            if (cartItem.getQuantity() > inventory.getQuantityInStock()) {

                CartIssueRes issue = new CartIssueRes();

                issue.setCartItemId(cartItem.getId());

                issue.setType("OUT_OF_STOCK");

                issue.setMessage("Only " + inventory.getQuantityInStock() + " items left");

                issues.add(issue);
            }

            BigDecimal lineTotal = product.getBasePrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            subtotal = subtotal.add(lineTotal);
        }

        return subtotal;
    }

    private BigDecimal calculateDiscount(BigDecimal subtotal, String promotionId, List<CartIssueRes> issues) {

        if (promotionId == null || promotionId.isBlank()) {

            return BigDecimal.ZERO;
        }

        Promotion promotion = promotionRepo.findById(promotionId).orElseThrow(() -> new MHException(MHErrors.PROMOTION_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(promotion.getStartAt()) || now.isAfter(promotion.getEndAt())) {
            CartIssueRes issue = new CartIssueRes();

            issue.setType("INVALID_PROMOTION");

            issue.setMessage("Promotion expired");

            issues.add(issue);

            return BigDecimal.ZERO;
        }

        BigDecimal discount;

        if (promotion.getDiscountType() == DiscountType.PERCENT) {

            discount = subtotal.multiply(

                    promotion.getDiscountValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

        } else {

            discount = promotion.getDiscountValue();
        }

        if (discount.compareTo(subtotal) > 0) {

            discount = subtotal;
        }

        return discount;
    }

    private Carrier randomCarrier() {

        List<Carrier> carriers = carrierRepo.findAllByStatus(CarrierStatus.ACTIVE);

        if (carriers.isEmpty()) {

            throw new MHException(MHErrors.CARRIER_NOT_FOUND);
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(carriers.size());

        return carriers.get(randomIndex);
    }

}

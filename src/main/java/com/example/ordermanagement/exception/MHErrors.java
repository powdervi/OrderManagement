package com.example.ordermanagement.exception;

import org.springframework.http.HttpStatus;

public class MHErrors {

    public static final MHBusinessError USER_NOT_FOUND = new MHBusinessError(404001, "User not found", HttpStatus.NOT_FOUND);

    public static final MHBusinessError ADDRESS_NOT_FOUND = new MHBusinessError(404001, "Adress not found", HttpStatus.NOT_FOUND);

    public static final MHBusinessError CATEGORY_NOT_FOUND = new MHBusinessError(404001, "CATEGORY_NOT_FOUND", HttpStatus.NOT_FOUND);

    public static final MHBusinessError USER_ALREADY_EXISTS = new MHBusinessError(400001, "User already exists", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError INVALID_REQUEST = new MHBusinessError(400002, "Invalid request", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError INTERNAL_SERVER_ERROR = new MHBusinessError(500000, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    public static final MHBusinessError PASS_WORD_IS_NOT_MATCH = new MHBusinessError(400000, "Password not matches", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError CATEGORY_DUPLICATED = new MHBusinessError(400000,"You can't assign a parent id to yourself", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError CATEGORY_LOOP = new MHBusinessError(400000, "CATEGORY_LOOP", HttpStatus.NOT_FOUND);

    public static final MHBusinessError CATEGORY_IS_NOT_LEAF = new MHBusinessError(400000, "Category is not leaf", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError PRODUCT_NOT_FOUND = new MHBusinessError(404001, "Product not found", HttpStatus.NOT_FOUND);

    public static final MHBusinessError INVENTORY_ALREADY_EXISTS = new MHBusinessError(400001, "Inventory already exists", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError INVENTORY_NOT_FOUND = new  MHBusinessError(404001, "Inventory not found", HttpStatus.NOT_FOUND);

    public static final MHBusinessError INVENTORY_NOT_ENOUGH = new MHBusinessError(400001, "Inventory not enough", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError CART_ALREADY_EXISTS = new MHBusinessError(400001, "Cart already exists", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError CART_NOT_FOUND = new MHBusinessError(404001, "Cart not found", HttpStatus.NOT_FOUND);

    public static final MHBusinessError OVER_STOCK = new MHBusinessError(400000, "Over stock", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError CART_ITEM_NOT_FOUND = new MHBusinessError(404001, "Cart item not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError EMAIL_ALREADY_EXITS = new MHBusinessError(400001, "Email already exists", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError PHONE_ALREADY_EXITS =  new  MHBusinessError(400001, "Phone already exists", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError OTP_ALREADY_VERIFIED = new  MHBusinessError(400001, "OTP already verified", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError OTP_EXPIRED = new  MHBusinessError(400001, "OTP expired", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError INVALID_OTP = new  MHBusinessError(400001, "Invalid OTP", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError CART_ITEM_REQUIRED = new  MHBusinessError(400001, "Cart item required", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError DUPLICATE_CART_ITEM = new MHBusinessError(400001, "Duplicate cart item", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError PROMOTION_NOT_FOUND = new  MHBusinessError(404001, "Promotion not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError INVALID_PROMOTION = new MHBusinessError(400001, "Invalid promotion", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError CARRIER_NOT_FOUND = new MHBusinessError(404001, "Carrier not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError OTP_NOT_FOUND = new  MHBusinessError(404001, "OTP not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError ORDER_ITEM_REQUIRED = new  MHBusinessError(400001, "Order item required", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError INVALID_QUANTITY = new MHBusinessError(400001, "Invalid quantity", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError DUPLICATE_PRODUCT = new MHBusinessError(400001, "Duplicate product", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError ADDRESS_REQUIRED = new MHBusinessError(400001, "Address required", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError PAYMENT_METHOD_REQUIRED = new MHBusinessError(400001, "Payment method required", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError OUT_OF_STOCK = new MHBusinessError(400001, "Out of stock", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError INVALID_PAYMENT_METHOD = new  MHBusinessError(400001, "Invalid payment method", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError PAYMENT_METHOD_NOT_FOUND = new MHBusinessError(404001, "Payment method not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError PRODUCT_INACTIVE = new MHBusinessError(400001, "Product inactive", HttpStatus.BAD_REQUEST);

    private MHErrors() {
    }
}

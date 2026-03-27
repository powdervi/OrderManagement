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

    private MHErrors() {
    }
}

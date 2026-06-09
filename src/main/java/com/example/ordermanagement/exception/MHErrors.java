package com.example.ordermanagement.exception;

import org.springframework.http.HttpStatus;

public class MHErrors {


    public static final MHBusinessError INTERNAL_SERVER_ERROR = new MHBusinessError(5009901, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final MHBusinessError INVALID_REQUEST = new MHBusinessError(4009901, "Invalid request", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError ACCESS_DENIED = new MHBusinessError(4039901, "Access denied", HttpStatus.FORBIDDEN);

    public static final MHBusinessError USER_NOT_FOUND = new MHBusinessError(4040101, "User not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError USER_ALREADY_EXISTS = new MHBusinessError(4000101, "User already exists", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError EMAIL_ALREADY_EXITS = new MHBusinessError(4000102, "Email already exists", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError PHONE_ALREADY_EXITS =  new MHBusinessError(4000103, "Phone already exists", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError PASS_WORD_IS_NOT_MATCH = new MHBusinessError(4000104, "Password not matches", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError OTP_NOT_FOUND = new MHBusinessError(4040102, "OTP not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError OTP_ALREADY_VERIFIED = new MHBusinessError(4000105, "OTP already verified", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError OTP_EXPIRED = new MHBusinessError(4000106, "OTP expired", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError INVALID_OTP = new MHBusinessError(4000107, "Invalid OTP", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError OTP_ALREADY_USED = new MHBusinessError(4000108, "OTP already used", HttpStatus.BAD_REQUEST);


    public static final MHBusinessError CATEGORY_NOT_FOUND = new MHBusinessError(4040201, "Category not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError CATEGORY_DUPLICATED = new MHBusinessError(4000201,"You can't assign a parent id to yourself", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError CATEGORY_LOOP = new MHBusinessError(4000202, "Category loop detected", HttpStatus.BAD_REQUEST); // Sửa thành 400 thay vì 404
    public static final MHBusinessError CATEGORY_IS_NOT_LEAF = new MHBusinessError(4000203, "Category is not leaf", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError PRODUCT_NOT_FOUND = new MHBusinessError(4040301, "Product not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError DUPLICATE_PRODUCT = new MHBusinessError(4000301, "Duplicate product", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError PRODUCT_INACTIVE = new MHBusinessError(4000302, "Product inactive", HttpStatus.BAD_REQUEST);
    // Inventory
    public static final MHBusinessError INVENTORY_NOT_FOUND = new MHBusinessError(4040302, "Inventory not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError INVENTORY_ALREADY_EXISTS = new MHBusinessError(4000303, "Inventory already exists", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError INVENTORY_NOT_ENOUGH = new MHBusinessError(4000304, "Inventory not enough", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError OVER_STOCK = new MHBusinessError(4000305, "Over stock", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError OUT_OF_STOCK = new MHBusinessError(4000306, "Out of stock", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError CART_NOT_FOUND = new MHBusinessError(4040401, "Cart not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError CART_ITEM_NOT_FOUND = new MHBusinessError(4040402, "Cart item not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError ADDRESS_NOT_FOUND = new MHBusinessError(4040403, "Address not found", HttpStatus.NOT_FOUND);

    public static final MHBusinessError CART_ALREADY_EXISTS = new MHBusinessError(4000401, "Cart already exists", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError CART_ITEM_REQUIRED = new MHBusinessError(4000402, "Cart item required", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError DUPLICATE_CART_ITEM = new MHBusinessError(4000403, "Duplicate cart item", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError INVALID_QUANTITY = new MHBusinessError(4000404, "Invalid quantity", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError ADDRESS_REQUIRED = new MHBusinessError(4000405, "Address required", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError ORDER_NOT_FOUND = new MHBusinessError(4040501, "Order not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError ORDER_ITEM_REQUIRED = new MHBusinessError(4000501, "Order item required", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError INVALID_ORDER_STATUS_TRANSITION = new MHBusinessError(4000502, "Invalid order status transition", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError ROLE_NOT_ALLOWED_TO_UPDATE_ORDER_STATUS = new MHBusinessError(4000503, "Role not allowed to update status", HttpStatus.BAD_REQUEST);

    public static final MHBusinessError PROMOTION_NOT_FOUND = new MHBusinessError(4040601, "Promotion not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError PAYMENT_METHOD_NOT_FOUND = new MHBusinessError(4040602, "Payment method not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError CARRIER_NOT_FOUND = new MHBusinessError(4040603, "Carrier not found", HttpStatus.NOT_FOUND);
    public static final MHBusinessError NOTIFICATION_TEMPLATE_NOT_FOUND = new MHBusinessError(4040604, "Notification template not found", HttpStatus.NOT_FOUND);

    public static final MHBusinessError INVALID_PROMOTION = new MHBusinessError(4000601, "Invalid promotion", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError PAYMENT_METHOD_REQUIRED = new MHBusinessError(4000602, "Payment method required", HttpStatus.BAD_REQUEST);
    public static final MHBusinessError INVALID_PAYMENT_METHOD = new MHBusinessError(4000603, "Invalid payment method", HttpStatus.BAD_REQUEST);
    private MHErrors() {
    }
}
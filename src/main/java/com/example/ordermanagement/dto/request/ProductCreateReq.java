package com.example.ordermanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductCreateReq {

    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "SKU chỉ được chứa chữ cái, chữ số và dấu gạch ngang (-)")
    @Size(max = 50, message = "SKU không được vượt quá 50 ký tự")
    private String sku;

    @NotBlank(message = "Danh mục không được để trống")
    @Size(max = 36, message = "ID Danh mục không hợp lệ")
    private String categoryId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 3, max = 255, message = "Tên sản phẩm phải từ 3 đến 255 ký tự")
    private String name;

    @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
    private String description;

    @NotNull(message = "Giá bán không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá bán không được là số âm")
    @Digits(integer = 13, fraction = 2, message = "Giá bán sai định dạng (tối đa 13 số phần nguyên và 2 số thập phân)")
    private BigDecimal basePrice;

    @DecimalMin(value = "0.0", inclusive = true, message = "Trọng lượng không được âm")
    @Digits(integer = 8, fraction = 2, message = "Trọng lượng sai định dạng")
    private BigDecimal weight;

    @Min(value = 0, message = "Số lượng tồn kho ban đầu không được nhỏ hơn 0")
    private Integer quantityInStock;
}
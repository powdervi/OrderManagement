package com.example.ordermanagement.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryUpdateReq {

    @Size(min = 2, max = 255, message = "Tên danh mục phải từ 2 đến 255 ký tự")
    private String name;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    @Size(max = 36, message = "ID Danh mục cha không hợp lệ")
    private String parentId;
}
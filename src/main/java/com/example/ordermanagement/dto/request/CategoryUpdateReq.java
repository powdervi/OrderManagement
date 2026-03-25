package com.example.ordermanagement.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateReq {

    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    private String description;

    private String parentId;
}

package com.example.ordermanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTreeRes {
    private String id;
    private String name;

    @JsonIgnore
    private String parentId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CategoryTreeRes> children = new ArrayList<>();

    public CategoryTreeRes(String id, String name, String parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
}
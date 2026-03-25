package com.example.ordermanagement.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FieldViolation {
    private String field;
    private String message;
}

package com.example.ordermanagement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class MHBusinessError {
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}

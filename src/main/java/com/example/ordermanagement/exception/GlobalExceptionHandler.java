package com.example.ordermanagement.exception;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.common.FieldViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MHException.class)
    public ResponseEntity<BaseResponse<Object>> handleMHException(MHException ex) {
        MHBusinessError error = ex.getErrorCode();

        return ResponseEntity
                .status(error.getHttpStatus())
                .body(BaseResponse.ofError(error.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        BaseResponse<Object> response = BaseResponse.ofError(400000, "Validation failed");

        List<FieldViolation> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldViolation)
                .toList();

        response.getMeta().setErrors(errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(MHErrors.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(BaseResponse.ofError(
                        MHErrors.INTERNAL_SERVER_ERROR.getCode(),
                        MHErrors.INTERNAL_SERVER_ERROR.getMessage()
                ));
    }

    private FieldViolation toFieldViolation(FieldError error) {
        return new FieldViolation(error.getField(), error.getDefaultMessage());
    }
}
package com.example.ordermanagement.exception;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.common.FieldViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
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

        log.warn("Business Exception - Code: {}, Message: {}", error.getCode(), ex.getMessage());

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
        log.warn("Validation Error: {}", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.ofError(400000, "Malformed JSON request. Please check your data format."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access Denied: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.ofError(403000, "Access denied. You do not have permission to access this resource."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication Failed: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.ofError(401000, "Authentication failed. Please login again."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleException(Exception ex) {
        log.error("Internal Server Error: ", ex);
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
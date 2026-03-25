package com.example.ordermanagement.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseResponse<T> {

    private T data;
    private Metadata meta = new Metadata();

    public static <T> BaseResponse<T> ofSuccess(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.data = data;
        response.meta.code = HttpStatus.OK.value();
        return response;
    }

    public static <T> BaseResponse<T> ofSuccess(String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.meta.code = HttpStatus.OK.value();
        response.meta.message = message;
        return response;
    }

    public static <T> BaseResponse<T> ofDeleteSuccess() {
        BaseResponse<T> response = new BaseResponse<>();
        response.meta.code = HttpStatus.NO_CONTENT.value();
        return response;
    }

    public static <T> BaseResponse<T> ofError(int code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.meta.code = code;
        response.meta.message = message;
        return response;
    }

    public static <T> BaseResponse<List<T>> ofSuccess(Page<T> page) {
        BaseResponse<List<T>> response = new BaseResponse<>();
        response.data = page.getContent();
        response.meta.code = HttpStatus.OK.value();
        response.meta.page = page.getNumber();
        response.meta.size = page.getSize();
        response.meta.total = page.getTotalElements();
        return response;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Metadata {
        private int code;
        private Integer page;
        private Integer size;
        private Long total;
        private String message;
        private List<FieldViolation> errors;
        private String requestId;
    }
}

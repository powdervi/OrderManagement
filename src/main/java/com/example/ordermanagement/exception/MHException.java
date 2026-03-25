package com.example.ordermanagement.exception;

import lombok.Getter;

@Getter
public class MHException extends RuntimeException {

    private final MHBusinessError errorCode;
    private final Object[] args;

    public MHException(MHBusinessError errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = new Object[0];
    }

    public MHException(MHBusinessError errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.args = new Object[0];
    }

    public MHException(MHBusinessError errorCode, Object... args) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = args != null ? args : new Object[0];
    }

    public MHException(MHBusinessError errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = new Object[0];
    }
}

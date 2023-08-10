package com.salam.ftth.config.exception;

import org.springframework.http.HttpStatus;

public enum AppErrors implements AppErrorStruct {
    // global errors
    FTTH_APP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong"),
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, ""),

    // app errors
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_EXISTS(HttpStatus.BAD_REQUEST, "User with this phone or email already exists"),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Request not found"),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password"),
    // guard errors
    NO_APPOINTMENTS_FOUND(HttpStatus.BAD_REQUEST, "No appointments found"),
    INVALID_OTP(HttpStatus.BAD_REQUEST, "Invalid OTP"),
    INVALID_STATE(HttpStatus.BAD_REQUEST, "Invalid state"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "Password mismatch");

    private final HttpStatus httpStatus;
    private final String message;
    private String messageKey;

    AppErrors(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    AppErrors(HttpStatus httpStatus, String message, String messageKey) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.messageKey = messageKey;
    }

    @Override
    public HttpStatus httpStatus() {
        return this.httpStatus;
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public String messageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}

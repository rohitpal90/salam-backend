package com.salam.dms.config.exception;

import org.springframework.http.HttpStatus;

public enum AppErrors implements AppErrorStruct {
    // global errors
    DMS_APP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong"),
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),

    // app errors
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Request not found"),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid otp or insufficient access rights"),
    BAD_TOKEN(HttpStatus.BAD_REQUEST, "Invalid token"),
    INSUFFICIENT_ACCESS(HttpStatus.FORBIDDEN, "Insufficient access rights"),
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "Plan not found"),

    // guard errors
    NO_APPOINTMENTS_FOUND(HttpStatus.BAD_REQUEST, "No appointments found"),
    CUSTOMER_OTP_INVALID(HttpStatus.BAD_REQUEST, "Invalid OTP"),
    INVALID_STATE(HttpStatus.BAD_REQUEST, "Invalid state");

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

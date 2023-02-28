package com.salam.dms.config.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AppErrors implements AppErrorStruct {
    // global errors
    DMS_APP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong"),
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, ""),

    // app errors
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Request not found"),
    NUMBER_FORMAT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Number format error"),
    INVALID_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Email format error"),
    INVALID_EMAIL_PASSWORD(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Email or Password"),
    INVALID_OTPT(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid TOTP or insufficient access rights"),
    // guard errors
    NO_APPOINTMENTS_FOUND(HttpStatus.BAD_REQUEST, "No appointments found"),
    CUSTOMER_OTP_INVALID(HttpStatus.BAD_REQUEST, "Invalid OTP"),
    INVALID_STATE(HttpStatus.BAD_REQUEST, "Invalid state");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return this.httpStatus;
    }

    @Override
    public String message() {
        return this.message;
    }

}

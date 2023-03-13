package com.salam.dms.config.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AppErrors implements AppErrorStruct {
    // global errors
    DMS_APP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong"),
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Form validation errors"),

    // app errors
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Request not found"),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid otp or insufficient access rights"),
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

package com.salam.dms.config.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AppErrors implements AppErrorStruct {
    // global errors
    DMS_APP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, ""),

    // app errors
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Request not found");

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

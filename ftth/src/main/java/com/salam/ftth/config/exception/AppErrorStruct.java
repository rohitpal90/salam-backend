package com.salam.ftth.config.exception;

import org.springframework.http.HttpStatus;

public interface AppErrorStruct {
    HttpStatus httpStatus();
    String message();

    default String messageKey() {
        return null;
    }

    Object args[] = new Object[0];
}


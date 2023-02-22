package com.salam.libs.exceptions;

public class RequestNotFoundException extends RuntimeException {
    private final String orderId;

    public RequestNotFoundException(String orderId) {
        this.orderId = orderId;
    }
}

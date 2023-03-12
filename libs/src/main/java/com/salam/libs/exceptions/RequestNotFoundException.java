package com.salam.libs.exceptions;

public class RequestNotFoundException extends RuntimeException {
    private String orderId;

    public RequestNotFoundException(String orderId) {
        this.orderId = orderId;
    }

    public RequestNotFoundException() {

    }
}

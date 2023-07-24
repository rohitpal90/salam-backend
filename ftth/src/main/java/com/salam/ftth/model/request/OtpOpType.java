package com.salam.ftth.model.request;

import lombok.Data;

public enum OtpOpType {
    REGISTER("register"),
    LOGIN("login");

    private String name;

    OtpOpType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

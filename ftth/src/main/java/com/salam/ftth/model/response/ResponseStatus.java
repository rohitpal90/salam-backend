package com.salam.ftth.model.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseStatus {
    SUCCESS("success"),
    FAILURE("failure");

    private String key;

    ResponseStatus(String key) {
        this.key = key;
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}

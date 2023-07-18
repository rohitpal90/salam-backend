package com.salam.ftth.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class ErrorInfo {

    private String message;
    private Map<String, Object> data;

    public ErrorInfo(String message) {
        this.message = message;
    }
}

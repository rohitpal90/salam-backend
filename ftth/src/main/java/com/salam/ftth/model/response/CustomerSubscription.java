package com.salam.ftth.model.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerSubscription {
    private String orderId;
    private String state;
    private JsonNode planInfo;
    private String createdAt;
}

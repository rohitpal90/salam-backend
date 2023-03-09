package com.salam.libs.sm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventData {
    private String event;

    public Message<String> toMessage() {
        return MessageBuilder.withPayload(event).build();
    }
}

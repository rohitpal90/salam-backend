package com.salam.dms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventData {
    private Event event;

    public Message<Event> toMessage() {
        return MessageBuilder.withPayload(event).build();
    }
}

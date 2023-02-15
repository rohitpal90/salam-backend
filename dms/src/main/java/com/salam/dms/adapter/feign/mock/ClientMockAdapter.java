package com.salam.dms.adapter.feign.mock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.dms.adapter.model.response.AppointmentResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class ClientMockAdapter {

    final Map<String, String> mocksMap;

    public <T> T getFor(String name, TypeReference<T> typeRef) {
        try {
            var mapper = new ObjectMapper();
            return mapper.readValue(mocksMap.get(name), typeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

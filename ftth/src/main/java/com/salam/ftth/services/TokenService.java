package com.salam.ftth.services;

import com.salam.ftth.adapter.feign.client.TokenClient;
import com.salam.ftth.adapter.model.response.CustomerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class TokenService {

    @Value("${token.username}")
    private String username;

    @Value("${token.password}")
    private String password;

    @Autowired
    TokenClient tokenClient;

    public String getToken() {
        CustomerResponse response = tokenClient.getToken(Map.of(
                "username", username,
                "password", password
        ));

        return response.getAccessToken();
    }

}

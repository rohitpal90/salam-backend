package com.salam.ftth.adapter.feign;

import com.salam.ftth.services.TokenService;
import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@EnableFeignClients
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor(TokenService tokenService) {
        return requestTemplate -> {
            requestTemplate.header(CONTENT_TYPE, APPLICATION_JSON_VALUE);
            requestTemplate.header(AUTHORIZATION, tokenService.getToken());
        };
    }

}

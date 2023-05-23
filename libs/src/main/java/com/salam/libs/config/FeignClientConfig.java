package com.salam.libs.config;

import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class FeignClientConfig {

    @Bean
    public Retryer retryer(@Value("${http.retry.period}") long period,
                           @Value("${http.retry.maxPeriod}") long maxPeriod,
                           @Value("${http.retry.maxAttempts}") int maxAttempts) {
        return new CustomRetryer(period, maxPeriod, maxAttempts);
    }

    @Bean
    public ErrorDecoder decoder() {
        return new CustomDecoder();
    }
}

@Slf4j
class CustomRetryer extends Retryer.Default {
    private final int maxAttempts;
    private final long period;
    private final long maxPeriod;

    public CustomRetryer(long period, long maxPeriod, int maxAttempts) {
        super(period, maxPeriod, maxAttempts);
        this.period = period;
        this.maxPeriod = maxPeriod;
        this.maxAttempts = maxAttempts;
    }

    private CustomRetryer(CustomRetryer retryer) {
        super(retryer.period, retryer.maxPeriod, retryer.maxAttempts);
        this.period = retryer.period;
        this.maxPeriod = retryer.maxPeriod;
        this.maxAttempts = retryer.maxAttempts;
    }

    @Override
    public void continueOrPropagate(RetryableException ex) {
        super.continueOrPropagate(ex);
        log.warn("Retrying HTTP request...");
    }

    @Override
    public Retryer clone() {
        return new CustomRetryer(this);
    }
}

class CustomDecoder implements ErrorDecoder {
    public Exception decode(String methodKey, Response response) {
        if (response.status() > 499) {
            throw new RetryableException(
                    response.status(),
                    String.format("Service unavailable (status code %s)", response.status()),
                    response.request().httpMethod(),
                    null,
                    response.request());
        } else {
            return new RuntimeException(
                    String.format("Error with status code %s received after HTTP request", response.status()));
        }
    }


}

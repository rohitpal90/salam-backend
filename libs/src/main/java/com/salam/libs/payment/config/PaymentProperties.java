package com.salam.libs.payment.config;

import com.salam.libs.feign.elm.config.SalamWebClientConfiguration;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Data
@Configuration
@AutoConfigureAfter(value = {SalamWebClientConfiguration.class})
@ConfigurationProperties(prefix = "salam.payment")
@ComponentScan("com.salam.libs.payment")
public class PaymentProperties {
    private String applicationId;
    private String applicationName;
}

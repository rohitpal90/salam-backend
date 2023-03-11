package com.salam.ftth;

import com.salam.libs.annotations.EnableSalamWorkflow;
import eu.fraho.spring.securityJwt.base.config.JwtSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSalamWorkflow
@SpringBootApplication(exclude = {JwtSecurityConfig.class})
public class FtthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtthApplication.class, args);
    }
}

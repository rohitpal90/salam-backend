package com.salam.ftth.config.db;

import eu.fraho.spring.securityJwt.hibernate.dto.RefreshTokenEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(
        basePackageClasses = {RefreshTokenEntity.class},
        basePackages = {"com.salam.ftth"})
public class DbConfig {

}

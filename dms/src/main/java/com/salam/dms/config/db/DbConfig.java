package com.salam.dms.config.db;

import eu.fraho.spring.securityJwt.hibernate.dto.RefreshTokenEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(
        basePackageClasses = {RefreshTokenEntity.class},
        basePackages = {"com.salam.dms"})
public class DbConfig {

}

package com.salam.libs.sm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        WorkflowProperties.class,
        StateMachineListenerConfig.class,
        StateMachineConfig.class,
})
@EntityScan(basePackages = "com.salam.libs.sm.entity")
public class StateMachineAutoConfiguration {

}

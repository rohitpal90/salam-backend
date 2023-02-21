package com.salam.libs.sm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StateMachineListenerConfig {

    @Bean
    public StateChangeListener listener(StateMachinePersisterDelegate persister) {
        return new StateChangeListener(persister);
    }

}

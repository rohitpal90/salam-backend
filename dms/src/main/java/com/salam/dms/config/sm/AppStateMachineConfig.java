package com.salam.dms.config.sm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppStateMachineConfig {

    @Bean
    public AppStateChangeListener listener(AppStateMachinePersisterDelegate persister) {
        return new AppStateChangeListener(persister);
    }

}

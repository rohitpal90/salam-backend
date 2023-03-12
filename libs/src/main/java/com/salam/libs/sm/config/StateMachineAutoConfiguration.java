package com.salam.libs.sm.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.statemachine.config.StateMachineFactory;

@Configuration
@Import({
        WorkflowProperties.class,
        StateMachineListenerConfig.class,
        StateMachineConfig.class,
})
@EntityScan(basePackages = "com.salam.libs.sm.entity")
public class StateMachineAutoConfiguration {

    @PersistenceContext
    EntityManager entityManager;


    @Bean
    public StateMachinePersist stateMachinePersist() {
        return new StateMachinePersist(entityManager);
    }

    @Bean
    public StateMachinePersisterDelegate stateMachinePersisterDelegate(StateMachinePersist stateMachinePersist) {
        return new StateMachinePersisterDelegate(stateMachinePersist, entityManager);
    }

    @Bean
    public StateChangeListener stateChangeListener(StateMachinePersisterDelegate persister) {
        return new StateChangeListener(persister);
    }

    @Bean
    public StateMachineAdapter stateMachineAdapter(StateMachineFactory<String, String> stateMachineFactory,
                                                   StateMachinePersisterDelegate persister,
                                                   WorkflowProperties workflowProperties) {
        return new StateMachineAdapter(stateMachineFactory, persister, workflowProperties);
    }
}

package com.salam.libs.sm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.List;


@Configuration
@EnableStateMachineFactory
public class AppStateMachineStateConfig extends StateMachineConfigurerAdapter<String, String> {

    @Autowired
    AppStateChangeListener listener;

    @Autowired
    List<? extends GuardHandler> guardHandlers;


    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config
                .withConfiguration()
                .listener(listener);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {
        // read for properties
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        // read all states
    }

    private GuardHandler getEventGuard(String event) {
        return this.guardHandlers.stream()
                .filter(it -> it.forType().equals(event))
                .map(GuardHandler.class::cast)
                .findFirst()
                .orElseGet(() -> new GuardHandler() {
                    @Override
                    public void handle(StateContext<String, String> context) {}

                    @Override
                    public String forType() {
                        return event;
                    }
                });
    }

}

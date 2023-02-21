package com.salam.libs.sm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    @Autowired
    StateChangeListener listener;

    @Autowired(required = false)
    List<? extends GuardHandler> guardHandlers;


    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config
                .withConfiguration()
                .listener(listener);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions.withExternal()
                .source("TEST1").target("TEST2")
                .event("EVENT1").guard(getEventGuard("EVENT1"));

        transitions.withExternal()
                .source("TEST3").target("TEST4")
                .event("EVENT2").guard(getEventGuard("EVENT2"));
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states
                .withStates()
                .initial("TEST1")
                .states(Set.of("TEST1", "TEST2", "TEST3", "TEST4"));
    }

    private GuardHandler getEventGuard(String event) {
        return Optional.ofNullable(this.guardHandlers)
                .orElseGet(Collections::emptyList)
                .stream()
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

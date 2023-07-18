package com.salam.libs.sm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Configuration
@EnableStateMachineFactory
@Import({StateMachineAdapter.class, StateMachinePersisterDelegate.class, StateMachinePersist.class})
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    @Autowired
    WorkflowProperties wfProps;

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
        for (WorkflowProperties.Transition transition : wfProps.getTransitions()) {
            transitions.withExternal()
                    .source(transition.getSource()).target(transition.getTarget())
                    .event(transition.getEvent()).guard(getEventGuard(transition.getEvent()));
        }
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states
                .withStates()
                .initial(wfProps.getInitialState())
                .states(wfProps.getStates());
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

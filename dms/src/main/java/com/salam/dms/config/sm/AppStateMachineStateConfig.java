package com.salam.dms.config.sm;

import com.salam.dms.model.Event;
import com.salam.dms.model.States;
import com.salam.dms.services.guards.VerifySmsGuard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;
import java.util.List;

import static com.salam.dms.model.Event.*;
import static com.salam.dms.model.States.*;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class AppStateMachineStateConfig extends EnumStateMachineConfigurerAdapter<States, Event> {

    @Autowired
    AppStateChangeListener listener;

    @Autowired
    List<? extends GuardHandler> guardHandlers;

    @Autowired
    VerifySmsGuard smsGuard;

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Event> config) throws Exception {
        config
                .withConfiguration()
                .listener(listener);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Event> transitions)
            throws Exception {
        transitions.withExternal()
                    .source(ACCOUNT_CREATION).target(MOBILE_VERIFICATION)
                    .event(CREATE_ACCOUNT).guard(getEventGuard(CREATE_ACCOUNT));

        transitions.withExternal()
                .source(ACCOUNT_CREATION).target(CANCELLED).event(CUSTOMER_CANCEL);

        transitions.withExternal()
                    .source(MOBILE_VERIFICATION).target(INSTALLATION_SCHEDULE)
                    .event(VERIFY_MOBILE).guard(getEventGuard(VERIFY_MOBILE));

        transitions.withExternal()
                .source(INSTALLATION_SCHEDULE).target(REQUEST_CONFIRMATION)
                .event(SCHEDULE).guard(getEventGuard(SCHEDULE));

//        transitions.withExternal()
//                .source(MOBILE_VERIFICATION).target(CANCELLED).event(CUSTOMER_CANCEL)
//                .and().withExternal()
//                .source(MOBILE_VERIFICATION).target(INSTALLATION_SCHEDULE).event(VERIFY_MOBILE)
//                .and().withExternal()
//                .source(MOBILE_VERIFICATION).target(CANCELLED).event(CUSTOMER_CANCEL)
//                .and().withExternal()
//                .source(INSTALLATION_SCHEDULE).target(REQUEST_CONFIRMATION).event(SCHEDULE)
//                .and().withExternal()
//                .source(INSTALLATION_SCHEDULE).target(CANCELLED).event(CUSTOMER_CANCEL)
//                .and().withExternal()
//                .source(REQUEST_CONFIRMATION).target(REVIEW).event(CUSTOMER_CONFIRMATION)
//                .and().withExternal()
//                .source(REQUEST_CONFIRMATION).target(CANCELLED).event(CUSTOMER_CANCEL)
//                .and().withExternal()
//                .source(REVIEW).target(INSTALLATION).event(CUSTOMER_CONFIRMATION)
//                .and().withExternal()
//                .source(REVIEW).target(CANCELLED).event(CUSTOMER_CANCEL)
//                .and().withExternal()
//                .source(REVIEW).target(REJECTED).event(REVIEW_REJECTED)
//                .and().withExternal()
//                .source(INSTALLATION).target(COMPLETED).event(COMPLETE)
//                .and().withExternal()
//                .source(INSTALLATION).target(CANCELLED).event(CUSTOMER_CANCEL);
    }

    @Bean
    public Guard<States, Event> guard() {
        return context -> false;
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Event> states) throws Exception {
        states
                .withStates()
                .initial(ACCOUNT_CREATION)
                .states(EnumSet.allOf(States.class));
    }

    private GuardHandler getEventGuard(Event event) {
        return this.guardHandlers.stream()
                .filter(it -> it.forType().equals(event))
                .map(GuardHandler.class::cast)
                .findFirst()
                .orElseGet(() -> new GuardHandler() {
                    @Override
                    public boolean handle(StateContext<States, Event> context) {
                        return false;
                    }

                    @Override
                    public Event forType() {
                        return event;
                    }
                });
    }

}

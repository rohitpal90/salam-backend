package com.salam.dms.config.sm;

import com.salam.dms.model.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.ObjectStateMachine;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.AbstractStateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateMachineAdapter {
    private final StateMachineFactory<States, Event> stateMachineFactory;
    private final AppStateMachinePersisterDelegate persister;

    private static final String ERROR_KEY = "error";

    @SneakyThrows
    public StateMachine<States, Event> restore(RequestContext requestContext) {
        var stateMachine = stateMachineFactory.getStateMachine();
        requestContext.setToStateMachineState(stateMachine);

        return persister.restore(stateMachine, requestContext);
    }

    @Transactional
    @SneakyThrows
    public void persist(StateContext<States, Event> stateContext) {
        persister.persist(stateContext);
    }

    @Transactional
    @SneakyThrows
    public void persist(StateMachine<States, Event> stateMachine) {
        persister.persist(stateMachine);
    }

    public Mono<EventResult> trigger(EventData eventData, RequestContext requestContext) {
        var stateMachine = restore(requestContext);
        var message = eventData.toMessage();
        return stateMachine.sendEvent(Mono.just(message))
                .map(result -> new EventResult(result, stateMachine))
                .next();
    }

    public Mono<EventResult> trigger(Event event, RequestContext requestContext) {
        return trigger(new EventData(event), requestContext);
    }

    public StateMachine<States, Event> create() {
        var stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.startReactively();

        return stateMachine;
    }

}

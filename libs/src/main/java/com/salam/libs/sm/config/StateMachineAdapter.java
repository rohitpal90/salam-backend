package com.salam.libs.sm.config;

import com.salam.libs.sm.entity.Request;
import com.salam.libs.sm.model.EventData;
import com.salam.libs.sm.model.EventResult;
import com.salam.libs.sm.model.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateMachineAdapter {
    private final StateMachineFactory<String, String> stateMachineFactory;
    private final StateMachinePersisterDelegate persister;
    private final WorkflowProperties workflowProperties;

    @SneakyThrows
    public StateMachine<String, String> restore(RequestContext requestContext) {
        return persister.restore(stateMachineFactory.getStateMachine(), requestContext);
    }

    @Transactional
    @SneakyThrows
    public void persist(StateContext<String, String> stateContext) {
        persister.persist(stateContext);
    }

    @Transactional
    @SneakyThrows
    public void persist(StateMachine<String, String> stateMachine) {
        persister.persist(stateMachine);
    }

    public Mono<EventResult> trigger(EventData eventData, RequestContext requestContext) {
        var stateMachine = restore(requestContext);
        var message = eventData.toMessage();
        return stateMachine.sendEvent(Mono.just(message))
                .map(result -> new EventResult(result, stateMachine))
                .next();
    }

    public Mono<EventResult> trigger(String event, RequestContext requestContext) {
        return trigger(new EventData(event), requestContext);
    }

    @SneakyThrows
    @Transactional
    public <T extends RequestContext> T newRequest(T reqContext) {
        var request = new Request();
        request.setState(workflowProperties.getInitialState());
        request.setMeta(reqContext.getMeta());
        request.setOrderId(reqContext.getOrderId());
        request = persister.createNew(request);

        reqContext.setRequestId(request.getId());
        reqContext.setOrderId(request.getOrderId());

        return reqContext;
    }

}

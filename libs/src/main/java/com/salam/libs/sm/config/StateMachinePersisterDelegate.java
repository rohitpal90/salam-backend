package com.salam.libs.sm.config;

import com.salam.libs.sm.entity.Request;
import com.salam.libs.sm.entity.Transition;
import com.salam.libs.sm.model.RequestContext;
import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class StateMachinePersisterDelegate {

    private final StateMachinePersister<String, String, RequestContext> persister;
    private final EntityManager entityManager;

    public void saveTransition(StateContext<String, String> stateContext, RequestContext requestContext) {
        var event = stateContext.getEvent();
        if (Objects.isNull(event)) {
            return;
        }

        String source = stateContext.getSource().getId();
        String target = stateContext.getTarget().getId();
        log.info("State changed from {} to {} for {}", source, target, requestContext.getOrderId());

        Long requestId = requestContext.getRequestId();

        var transition = Transition.builder()
                .name(event)
                .payload(requestContext.getMeta())
                .requestId(requestId)
                .from(source)
                .to(target)
                .build();

        entityManager.persist(transition);
    }

    @Autowired
    public StateMachinePersisterDelegate(StateMachinePersist persist, EntityManager entityManager) {
        this.persister = new DefaultStateMachinePersister<>(persist);
        this.entityManager = entityManager;
    }

    @SneakyThrows
    @Transactional
    public void persist(StateMachine<String, String> stateMachine) {
        var requestContext = RequestContext.fromStateMachine(stateMachine);
        persister.persist(stateMachine, requestContext);
    }

    @SneakyThrows
    @Transactional
    public void persist(StateContext<String, String> stateContext) {
        var stateMachine = stateContext.getStateMachine();
        var requestContext = RequestContext.fromStateMachine(stateMachine);

        saveTransition(stateContext, requestContext);
        persister.persist(stateMachine, requestContext);
    }

    @SneakyThrows
    public StateMachine<String, String> restore(StateMachine<String, String> stateMachine,
                                               RequestContext requestContext) {
        return persister.restore(stateMachine, requestContext);
    }

    @Transactional
    public Request createNew(Request request) {
        return entityManager.merge(request);
    }
}

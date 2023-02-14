package com.salam.dms.config.sm;

import com.salam.dms.db.entity.Transition;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.States;
import com.salam.dms.repos.TransitionRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AppStateMachinePersisterDelegate {

    private final StateMachinePersister<States, Event, RequestContext> persister;
    private final TransitionRepository transitionRepository;

    @Autowired
    public AppStateMachinePersisterDelegate(AppStateMachinePersist persist,
                                            TransitionRepository transitionRepository) {
        this.persister = new DefaultStateMachinePersister<>(persist);
        this.transitionRepository = transitionRepository;
    }

    @SneakyThrows
    @Transactional
    public void persist(StateMachine<States, Event> stateMachine) {
        var requestContext = RequestContext.fromStateMachine(stateMachine);
        persister.persist(stateMachine, requestContext);
    }

    @SneakyThrows
    @Transactional
    public void persist(StateContext<States, Event> stateContext) {
        var stateMachine = stateContext.getStateMachine();
        var requestContext = RequestContext.fromStateMachine(stateMachine);
        var event = stateContext.getEvent();

        States source = stateContext.getSource().getId();
        States target = stateContext.getTarget().getId();
        log.info("State changed from {} to {}", source, target);

        Long requestId = requestContext.getRequestId();

        // TODO: move to transition service
        var transition = Transition.builder()
                .name(event)
                .payload("{}")
                .requestId(requestId)
                .from(source)
                .to(target)
                .build();

        transitionRepository.save(transition);
        persister.persist(stateMachine, requestContext);
    }

    @SneakyThrows
    public StateMachine<States, Event> restore(StateMachine<States, Event> stateMachine,
                                               RequestContext requestContext) {
        return persister.restore(stateMachine, requestContext);
    }
}

package com.salam.dms.config.sm;

import com.salam.dms.model.Event;
import com.salam.dms.model.States;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppStateChangeListener extends StateMachineListenerAdapter<States, Event> {

    private final AppStateMachinePersisterDelegate persister;

    @Override
    public void eventNotAccepted(Message<Event> event) {
        log.error("Event not : {}", event.getPayload());
    }

    @Override
    @SneakyThrows
    @Transactional
    public void stateContext(StateContext<States, Event> stateContext) {
        switch (stateContext.getStage()) {
            case STATE_CHANGED -> persister.persist(stateContext);
            default -> {}
        }
    }

}

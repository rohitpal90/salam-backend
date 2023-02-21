package com.salam.libs.sm.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppStateChangeListener extends StateMachineListenerAdapter<String, String> {

    private final AppStateMachinePersisterDelegate persister;


    @Override
    public void eventNotAccepted(Message<String> event) {
        log.error("Event not : {}", event.getPayload());
    }

    @Override
    @SneakyThrows
    @Transactional
    public void stateContext(StateContext<String, String> stateContext) {
        switch (stateContext.getStage()) {
            case STATE_CHANGED -> persister.persist(stateContext);
            default -> {}
        }
    }

}

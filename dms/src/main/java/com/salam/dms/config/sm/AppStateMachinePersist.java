package com.salam.dms.config.sm;

import com.salam.dms.db.entity.Request;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.States;
import com.salam.dms.repos.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppStateMachinePersist implements StateMachinePersist<States, Event, RequestContext> {

    private final RequestRepository requestRepository;

    @Override
    public void write(StateMachineContext<States, Event> context, RequestContext requestContext) {
        Request request = requestRepository.findById(requestContext.getRequestId()).orElseThrow();
        request.setState(context.getState());
        request.setMeta(requestContext.getMetaInfo());

        requestRepository.save(request);
    }

    @Override
    public StateMachineContext<States, Event> read(RequestContext requestContext) {
        Request request = requestRepository.findById(requestContext.getRequestId()).orElseThrow();
        return new StateMachineContext<>() {
            @Override
            public String getId() {
                return request.getId().toString();
            }

            @Override
            public List<StateMachineContext<States, Event>> getChilds() {
                return Collections.emptyList();
            }

            @Override
            public List<String> getChildReferences() {
                return Collections.emptyList();
            }

            @Override
            public States getState() {
                return request.getState();
            }

            @Override
            public Event getEvent() {
                return null;
            }

            @Override
            public Map<States, States> getHistoryStates() {
                return null;
            }

            @Override
            public Map<String, Object> getEventHeaders() {
                return null;
            }

            @Override
            public ExtendedState getExtendedState() {
                return null;
            }
        };
    }
}

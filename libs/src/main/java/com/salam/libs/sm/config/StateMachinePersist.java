package com.salam.libs.sm.config;

import com.salam.libs.sm.entity.Request;
import com.salam.libs.sm.model.RequestContext;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StateMachinePersist implements org.springframework.statemachine.StateMachinePersist<String, String, RequestContext> {

    @PersistenceContext
    EntityManager entityManager;


    private Optional<Request> getRequestById(Long requestId) {
        TypedQuery<Request> query = entityManager.createQuery("from Request r where r.id = :requestId",
                Request.class);
        query.setParameter("requestId", requestId);

        return query.getResultList().stream().findFirst();
    }

    @Override
    public void write(StateMachineContext<String, String> context, RequestContext requestContext) {
        var request = getRequestById(requestContext.getRequestId()).orElseThrow();

        request.setState(context.getState());
        request.setMeta(requestContext.getMeta());

        entityManager.persist(request);
    }

    @Override
    public StateMachineContext<String, String> read(RequestContext requestContext) {
        Request request = getRequestById(requestContext.getRequestId()).orElseThrow();
        return new StateMachineContext<>() {
            @Override
            public String getId() {
                return request.getId().toString();
            }

            @Override
            public List<StateMachineContext<String, String>> getChilds() {
                return Collections.emptyList();
            }

            @Override
            public List<String> getChildReferences() {
                return Collections.emptyList();
            }

            @Override
            public String getState() {
                return request.getState();
            }

            @Override
            public String getEvent() {
                return null;
            }

            @Override
            public Map<String, String> getHistoryStates() {
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

package com.salam.libs.sm.config;

import com.salam.libs.exceptions.RequestNotFoundException;
import com.salam.libs.sm.entity.Request;
import com.salam.libs.sm.model.RequestContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StateMachinePersist implements org.springframework.statemachine.StateMachinePersist<String, String, RequestContext> {

    private final EntityManager entityManager;


    private Optional<Request> getRequestById(Long requestId) {
        TypedQuery<Request> query = entityManager.createQuery("from Request r where r.id = :requestId",
                Request.class);
        query.setParameter("requestId", requestId);

        return query.getResultList().stream().findFirst();
    }

    private Optional<Request> getRequestByOrderId(String orderId) {
        TypedQuery<Request> query = entityManager.createQuery("from Request r where r.orderId = :orderId",
                Request.class);
        query.setParameter("orderId", orderId);

        return query.getResultList().stream().findFirst();
    }

    @Override
    @SneakyThrows
    public void write(StateMachineContext<String, String> context, RequestContext requestContext) {
        var request = getRequestById(requestContext.getRequestId()).orElseThrow(() ->
                new RequestNotFoundException(requestContext.getOrderId()));

        request.setState(context.getState());
        request.setMeta(
                requestContext.getMetaRaw()
        );

        entityManager.persist(request);
    }

    @Override
    @SneakyThrows
    public StateMachineContext<String, String> read(RequestContext requestContext) {
        String orderId = requestContext.getOrderId();
        Request request = getRequestByOrderId(orderId)
                .orElseThrow(() -> new RequestNotFoundException(orderId));
        requestContext.setOrderId(request.getOrderId());
        requestContext.setRequestId(request.getId());
        requestContext.setUserId(request.getUserId());
        requestContext.setMetaRaw(
                request.getMeta()
        );

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
                return new HashMap<>() {
                    {
                        put("requestId", request.getId());
                        put("orderId", request.getOrderId());
                    }
                };
            }

            @Override
            public ExtendedState getExtendedState() {
                var variables = new HashMap<Object, Object>();
                variables.put(RequestContext.KEY, requestContext);
                return new DefaultExtendedState(variables);
            }
        };
    }
}

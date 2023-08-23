package com.salam.ftth.services;

import com.salam.ftth.model.Event;
import com.salam.ftth.model.PlanInfo;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.RequestMetaInfo;
import com.salam.ftth.model.request.CustomerProfileRequest;
import com.salam.ftth.model.response.EventResult;
import com.salam.libs.sm.config.StateMachineAdapter;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StateMachineService {
    private final StateMachineAdapter stateMachineAdapter;

    public EventResult trigger(Event event, RequestContext requestContext) {
        var eventResult = stateMachineAdapter.trigger(event.name(), requestContext).block();
        return new EventResult(eventResult);
    }

    private final JwtUser wfUser;

    @PersistenceContext
    private EntityManager em;

    private static final DateTimeFormatter ORDER_DATE_FMT = DateTimeFormatter.ofPattern("yyMMdd");


    public String generateOrderId() {
        var currentOrderCount = (Long) em.createQuery(
                        "select count(r) from Request r where r.userId = :userId")
                .setParameter("userId", wfUser.getId())
                .getSingleResult();
        var prefix = ZonedDateTime.now().format(ORDER_DATE_FMT);

        return String.format("%s%d%09d", prefix, wfUser.getId(), currentOrderCount + 1);
    }

    public Optional<?> fetchRequest(String requestId) {
        var requests = em.createQuery(
                        "select r from Request r where r.userId = :userId " +
                                "and r.orderId = :orderId")
                .setParameter("userId", wfUser.getId())
                .setParameter("orderId", requestId)
                .setMaxResults(1)
                .getResultList();

        return requests.stream().findFirst();
    }

    public RequestContext initiate(CustomerProfileRequest customerInfo, PlanInfo planInfo) {
        var orderId = generateOrderId();
        var metaInfo = new RequestMetaInfo();
        metaInfo.setCustomerInfo(customerInfo);
        metaInfo.setPlanInfo(planInfo);

        var requestContext = new RequestContext(orderId, wfUser.getId());
        requestContext.setMeta(metaInfo);

        var sm = stateMachineAdapter.create(requestContext);
        return RequestContext.fromStateMachine(sm);
    }

    public RequestContext restore(RequestContext requestContext) {
        var sm = stateMachineAdapter.restore(requestContext);
        return RequestContext.fromStateMachine(sm);
    }

    public StateMachine<String, String> getSm(RequestContext requestContext) {
        return stateMachineAdapter.restore(requestContext);
    }

    public void persist(RequestContext requestContext) {
        var sm = this.getSm(new RequestContext(requestContext.getOrderId(), requestContext.getUserId()));
        sm = requestContext.setToStateMachineState(sm);
        stateMachineAdapter.persist(sm);
    }
}

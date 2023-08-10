package com.salam.ftth.services;

import com.salam.ftth.db.entity.User;
import com.salam.ftth.model.Event;
import com.salam.ftth.model.PlanInfo;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.RequestMetaInfo;
import com.salam.ftth.model.request.CustomerProfileRequest;
import com.salam.ftth.model.request.IDType;
import com.salam.ftth.model.request.RegisterRequest;
import com.salam.libs.sm.config.StateMachineAdapter;
import com.salam.libs.sm.model.EventResult;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StateMachineService {
    private final StateMachineAdapter stateMachineAdapter;

    public EventResult trigger(Event event, RequestContext requestContext) {
        return stateMachineAdapter.trigger(event.name(), requestContext).block();
    }

    private final User wfUser;

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

    public Optional<Object> fetchRequest(String requestId, Long userId) {
        var request = em.createQuery(
                        "select r from Request r where r.userId = :userId " +
                                "and r.orderId = :orderId")
                .setParameter("userId", wfUser.getId())
                .setParameter("orderId", requestId)
                .getSingleResult();

        return Optional.of(request);
    }

    public RequestContext initiate(RegisterRequest registerRequest,
                                   PlanInfo planInfo,
                                   JwtUser author) {
        var orderId = generateOrderId();
        var metaInfo = new RequestMetaInfo();

        var customerInfo = new CustomerProfileRequest();
        customerInfo.setId(registerRequest.getId());
        customerInfo.setMobile(registerRequest.getMobile());
        customerInfo.setDob(registerRequest.getDob());
        customerInfo.setIdType(IDType.valueOf(registerRequest.getIdType()));
        customerInfo.setFullName(registerRequest.getFullName());
        customerInfo.setUsername(registerRequest.getUsername());
        customerInfo.setEmail(registerRequest.getEmail());
        metaInfo.setCustomerInfo(customerInfo);

        metaInfo.setPlanInfo(planInfo);

        var requestContext = new RequestContext(orderId, author.getId());
        requestContext.setMeta(metaInfo);

        var sm = stateMachineAdapter.create(requestContext);
        return RequestContext.fromStateMachine(sm);
    }

    public RequestContext restore(RequestContext requestContext) {
        var sm = stateMachineAdapter.restore(requestContext);
        return RequestContext.fromStateMachine(sm);
    }
}

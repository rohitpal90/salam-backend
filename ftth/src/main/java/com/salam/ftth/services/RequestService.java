package com.salam.ftth.services;

import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.db.entity.User;
import com.salam.ftth.model.PlanInfo;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.RequestMetaInfo;
import com.salam.ftth.model.request.CustomerProfileRequest;
import com.salam.libs.sm.config.StateMachineAdapter;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.salam.ftth.config.exception.AppErrors.REQUEST_NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
public class RequestService {

    private final StateMachineService stateMachineService;
    private final JwtUser wfUser;

    public RequestContext fetchRequest(String requestId, JwtUser user) {
        var request = stateMachineService
                .fetchRequest(requestId, user.getId())
                .orElseThrow(() -> AppError.create(REQUEST_NOT_FOUND));

        RequestContext requestContext = new RequestContext(requestId, wfUser.getId());
        requestContext.setActor(user);

        return requestContext;
    }
}

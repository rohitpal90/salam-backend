package com.salam.ftth.services;

import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.model.RequestContext;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.salam.ftth.config.exception.AppErrors.REQUEST_NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
public class RequestService {

    private final StateMachineService stateMachineService;
    private final JwtUser wfUser;

    public RequestContext fetchRequest(String requestId) {
        var request = stateMachineService
                .fetchRequest(requestId, wfUser.getId())
                .orElseThrow(() -> AppError.create(REQUEST_NOT_FOUND));

        RequestContext requestContext = new RequestContext(requestId, wfUser.getId());
        requestContext.setActor(wfUser);

        return requestContext;
    }
}

package com.salam.dms.services;

import com.salam.dms.db.entity.Request;
import com.salam.dms.model.PlanInfo;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.States;
import com.salam.dms.model.request.CustomerProfileRequest;
import com.salam.dms.repos.RequestRepository;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class RequestService {

    private final RequestRepository requestRepository;

    public RequestContext initiate(CustomerProfileRequest profileRequest,
                                   PlanInfo planInfo, JwtUser user) {
        var requestContext = RequestContext.createNew(profileRequest);
        requestContext.setPlanInfo(planInfo);
        requestContext.setActor(user);

        var request = new Request();
        request.setState(States.ACCOUNT_CREATION);
        request.setMeta(requestContext.getMetaInfo());
        request.setWorkflowId(1L);
        requestRepository.save(request);

        requestContext.setRequestId(request.getId());
        return requestContext;
    }

}

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
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private static final DateTimeFormatter ORDER_DATE_FMT = DateTimeFormatter.ofPattern("yyMMdd");

    public RequestContext initiateOrGet(CustomerProfileRequest profileRequest, PlanInfo planInfo,
                                        JwtUser user, String requestId) {
        // load existing
        if (StringUtils.hasText(requestId)) {
            return fetchRequest(requestId, user);
        }

        // create new
        var requestContext = RequestContext.createNew(profileRequest);
        requestContext.setCustomerProfileRequest(profileRequest);
        requestContext.setPlanInfo(planInfo);
        requestContext.setActor(user);

        var request = new Request();
        request.setState(States.ACCOUNT_CREATION);
        request.setMeta(requestContext.getMetaInfo());
        request.setUserId(user.getId());
        request.setOrderId(generateOrderId(request));
        requestRepository.save(request);

        requestContext.setRequestId(request.getId());
        requestContext.setOrderId(request.getOrderId());
        return requestContext;
    }

    public String generateOrderId(Request request) {
        var currentOrderCount = requestRepository.countByUserId(request.getUserId());
        var prefix = ZonedDateTime.now().format(ORDER_DATE_FMT);

        return String.format("%s%010d", prefix, currentOrderCount + 1);
    }

    public RequestContext fetchRequest(String requestId, JwtUser user) {
        var request = requestRepository
                .findByOrderIdAndUserId(requestId, user.getId())
                .orElseThrow();

        var requestContext = RequestContext.fromRequest(request);
        requestContext.setActor(user);

        return requestContext;
    }


}

package com.salam.dms.model.mappers;

import com.salam.dms.model.RequestContext;
import com.salam.dms.repos.RequestRepository;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@Component
public class RequestHeaderConverter implements Converter<String, RequestContext> {

    @Autowired
    RequestRepository requestRepository;

    @Override
    public RequestContext convert(String requestId) {
        var request = requestRepository.findById(Long.parseLong(requestId)).orElseThrow();

        var requestContext =  RequestContext.fromRequest(request);
        JwtUser actor = Objects.requireNonNull(getPrincipalUser());
        requestContext.setActor(actor);

        return requestContext;
    }

    private JwtUser getPrincipalUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtUser) authentication.getPrincipal();
    }

    public static boolean matchMismatchError(MethodArgumentTypeMismatchException e) {
        return e.getName().equals("reqId") && e.getParameter()
                .getParameterType().equals(RequestContext.class);
    }

}

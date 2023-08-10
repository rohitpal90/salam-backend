package com.salam.ftth.model.mappers;

import com.salam.ftth.model.RequestContext;
import com.salam.ftth.services.RequestService;
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
    RequestService requestService;

    @Override
    public RequestContext convert(String requestId) {
        JwtUser actor = Objects.requireNonNull(getPrincipalUser());
        return requestService.fetchRequest(requestId, actor);
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
package com.salam.ftth.model.mappers;

import com.salam.ftth.model.RequestContext;
import com.salam.ftth.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Component
public class RequestHeaderConverter implements Converter<String, RequestContext> {

    @Autowired
    RequestService requestService;

    @Override
    public RequestContext convert(String requestId) {
        return requestService.fetchRequest(requestId);
    }

    public static boolean matchMismatchError(MethodArgumentTypeMismatchException e) {
        return e.getName().equals("reqId") && e.getParameter()
                .getParameterType().equals(RequestContext.class);
    }

}
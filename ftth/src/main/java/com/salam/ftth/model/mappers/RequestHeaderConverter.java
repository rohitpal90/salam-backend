package com.salam.ftth.model.mappers;

import com.salam.ftth.config.auth.UserDetailService;
import com.salam.ftth.db.entity.User;
import com.salam.ftth.model.RequestContext;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;

@Component
@AllArgsConstructor
public class RequestHeaderConverter implements Converter<String, RequestContext> {

    final User wfUser;


    @Override
    public RequestContext convert(String requestId) {
        Optional<JwtUser> actorOpt = getPrincipalUser();
        var actor = actorOpt.orElseGet(() -> UserDetailService.buildJwtUser(wfUser));

        var requestContext =  new RequestContext(requestId, actor.getId());
        requestContext.setActor(actor);
        return requestContext;
    }

    private Optional<JwtUser> getPrincipalUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
                .flatMap(it -> {
                    var user = it.getPrincipal();
                    if (user instanceof JwtUser) {
                        return Optional.of((JwtUser) user);
                    }

                    return Optional.empty();
                });
    }

    public static boolean matchMismatchError(MethodArgumentTypeMismatchException e) {
        return e.getName().equals("reqId") && e.getParameter()
                .getParameterType().equals(RequestContext.class);
    }

}

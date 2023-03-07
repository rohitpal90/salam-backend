package com.salam.dms.services;

import com.salam.dms.db.entity.Transition;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.States;
import com.salam.dms.repos.TransitionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TransitionService {

    final TransitionRepository transitionRepository;

    public void saveTransition(StateContext<States, Event> stateContext, RequestContext requestContext) {
        var event = stateContext.getEvent();

        States source = stateContext.getSource().getId();
        States target = stateContext.getTarget().getId();
        log.info("State changed from {} to {} for {}", source, target, requestContext.getOrderId());

        Long requestId = requestContext.getRequestId();

        var transition = Transition.builder()
                .name(event)
                .payload(requestContext.getMetaInfo())
                .requestId(requestId)
                .from(source)
                .to(target)
                .build();

        transitionRepository.save(transition);
    }

}

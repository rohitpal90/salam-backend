package com.salam.dms.controllers;

import com.salam.dms.config.sm.StateMachineAdapter;
import com.salam.dms.model.Event;
import com.salam.dms.model.EventResult;
import com.salam.dms.model.RequestContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/dealers")
@Tag(name = "Dealer")
public class DealerController {

    private final StateMachineAdapter stateMachineAdapter;

    @PostMapping("/installed")
    public EventResult rejectRequest(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineAdapter.trigger(Event.INSTALLED, requestContext).block();
    }

    @PostMapping("/review")
    public EventResult reviewRequest(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineAdapter.trigger(Event.REVIEW_REQUEST, requestContext).block();
    }

}

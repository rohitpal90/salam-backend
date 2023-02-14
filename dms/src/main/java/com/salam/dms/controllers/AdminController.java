package com.salam.dms.controllers;

import com.salam.dms.config.sm.StateMachineAdapter;
import com.salam.dms.model.Event;
import com.salam.dms.model.EventResult;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.request.AppointmentBookRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final StateMachineAdapter stateMachineAdapter;

    @PostMapping("/approve")
    public EventResult approveRequest(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineAdapter.trigger(Event.APPROVE, requestContext).block();
    }

    @PostMapping("/reject")
    public EventResult rejectRequest(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineAdapter.trigger(Event.REJECT, requestContext).block();
    }

}

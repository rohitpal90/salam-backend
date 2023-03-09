package com.salam.ftth.controllers;

import com.salam.ftth.model.Event;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.response.EventResult;
import com.salam.ftth.services.StateMachineService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/dealers")
public class DealerController {

    private final StateMachineService stateMachineService;

    @PostMapping("/installed")
    public EventResult rejectRequest(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineService.trigger(Event.INSTALLED, requestContext);
    }

}

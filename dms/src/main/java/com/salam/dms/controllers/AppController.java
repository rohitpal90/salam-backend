package com.salam.dms.controllers;

import com.salam.dms.config.sm.StateMachineAdapter;
import com.salam.dms.model.EventData;
import com.salam.dms.model.EventResult;
import com.salam.dms.model.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/sm")
@RequiredArgsConstructor
public class AppController {

    private final StateMachineAdapter stateMachineAdapter;

    @PostMapping("/events")
    public Mono<EventResult> events(@RequestHeader("reqId") RequestContext requestContext,
                                     @RequestBody EventData eventData) {
        return stateMachineAdapter.trigger(eventData, requestContext);
    }

}

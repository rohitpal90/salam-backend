package com.salam.dms.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/request")
public class RequestController {

    // TODO: request id from jwt
    @GetMapping
    public Object getReqInfo() {
        return null;
    }

}

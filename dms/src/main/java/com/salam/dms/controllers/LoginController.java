package com.salam.dms.controllers;

import com.salam.dms.services.UserService;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class LoginController {

    final UserService userService;


    @PostMapping("/login/1")
    public Object checkLogin(@RequestBody @Valid AuthenticationRequest request) {
        userService.performLoginStep1(request);
        return Map.of("message", "success");
    }

}

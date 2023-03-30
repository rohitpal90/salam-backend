package com.salam.dms.controllers;

import com.salam.dms.services.UserService;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication")
public class LoginController {

    final UserService userService;


    @PostMapping("/login/1")
    @Operation(
            summary = "Login step 1",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "LoginResponse"),
                    @ApiResponse(responseCode = "404", ref = "LoginFailureResponse")
            }
    )
    public Object checkLogin(@RequestBody @Valid AuthenticationRequest request) {
        userService.performLoginStep1(request);
        return Map.of("message", "success");
    }
}

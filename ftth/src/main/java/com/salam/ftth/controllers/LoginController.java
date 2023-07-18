package com.salam.ftth.controllers;

import com.salam.ftth.config.auth.LoginService;
import com.salam.ftth.model.request.LoginRequest;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class LoginController {

    final LoginService loginService;


    @PostMapping("/login")
    @Operation(
            summary = "Login",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "SuccessResponse"),
                    @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
                    @ApiResponse(responseCode = "404", ref = "LoginFailureResponse"),
            },
            requestBody = @RequestBody(content = @Content(examples = {
                    @ExampleObject(ref = "LoginRequest", name = "LoginRequest")
            }))
    )
    public Object checkLogin(@org.springframework.web.bind.annotation.RequestBody
                             @Valid LoginRequest loginRequest) {
        loginService.checkLogin(loginRequest);
        return Map.of("message", "success");
    }

    @PostMapping("/verify/otp")
    @Operation(
            summary = "Verify Customer Otp",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "SuccessResponse"),
                    @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
                    @ApiResponse(responseCode = "404", ref = "LoginFailureResponse"),
            },
            requestBody = @RequestBody(content = @Content(examples = {
                    @ExampleObject(ref = "LoginVerifyRequest", name = "LoginVerifyRequest")
            }))
    )
    public AuthenticationResponse login(@org.springframework.web.bind.annotation.RequestBody
                                        @Validated(LoginRequest.LoginRequestStep2.class)
                                        LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }
}

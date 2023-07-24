package com.salam.ftth.controllers;

import com.salam.ftth.model.request.OtpVerifyRequest;
import com.salam.ftth.services.otp.OtpHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/otp")
public class OtpController {
    private final ApplicationContext applicationContext;

    @PostMapping("/verify")
    @Operation(
            summary = "Verify otp",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "RegisterOtpSuccessResponse", ref = "RegisterOtpSuccessResponse"),
                                    @ExampleObject(name = "LoginOtpSuccessResponse", ref = "LoginOtpSuccessResponse"),
                            }
                    )),
                    @ApiResponse(responseCode = "400", content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "BadOtpResponse", ref = "BadOtpResponse"),
                                    @ExampleObject(name = "BadRequestResponse", ref = "BadRequestResponse"),
                            }
                    )),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(ref = "LoginOtpVerifyRequest", name = "LoginOtpVerifyRequest"),
                    @ExampleObject(ref = "RegisterOtpVerifyRequest", name = "RegisterOtpVerifyRequest"),
            }))
    )
    public Object verifyOtp(@org.springframework.web.bind.annotation.RequestBody
                            @Valid OtpVerifyRequest request) {
        var otpHandler = applicationContext.getBeansOfType(OtpHandler.class)
                .get(String.format("%sOtpHandler", request.getOperation()));
        return otpHandler.handle(request);
    }
}

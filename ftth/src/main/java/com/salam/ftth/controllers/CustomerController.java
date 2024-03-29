package com.salam.ftth.controllers;

import com.salam.ftth.model.request.RegisterRequest;
import com.salam.ftth.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    @Operation(
            summary = "Register customer",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "SuccessResponse"),
                    @ApiResponse(responseCode = "400", content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "BadRequestResponse", ref = "BadRequestResponse"),
                                    @ExampleObject(name = "UserAlreadyExistResponse", ref = "UserAlreadyExistResponse"),
                            }
                    )),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(ref = "RegisterRequest", name = "RegisterRequest"),
            }))
    )
    public Object registerCustomer(@RequestBody @Valid RegisterRequest registerRequest) {
        customerService.register(registerRequest);
        return Map.of("message", "success");
    }
}

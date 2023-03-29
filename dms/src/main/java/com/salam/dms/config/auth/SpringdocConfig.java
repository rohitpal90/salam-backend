package com.salam.dms.config.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

@OpenAPIDefinition
@Configuration
@SecurityScheme(
        name = "Bearer-token",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class SpringdocConfig {

    @Bean
    public OpenAPI baseOpenAPI() throws IOException {
        ReadJsonFromFile json = new ReadJsonFromFile();


        ApiResponse loginResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("LoginSuccessResponse").toString())))
        ).description("Login Success Response");

        ApiResponse loginFailureResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("LoginFailureResponse").toString())))
        ).description("Login is failed due to  user not found ");

        ApiResponse unauthenticatedResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("Unauthenticated").toString())))
        ).description("you are not authenticated to access api");

        ApiResponse CustomerResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("CustomerSuccess").toString())))
        ).description("Account creation Success response");


        ApiResponse AppointmentResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("AppointmentResponse").toString())))
        ).description("List of Appointment Response");

        ApiResponse verifyResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("VerifySuccess").toString())))
        ).description("Verify Mobile Correct Response");

        ApiResponse verifyFailureResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("VerifyBadResponse").toString())))
        ).description("For invalid otp");

        ApiResponse notFoundResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("RequestNotFound").toString())))
        ).description("Request is not available");

        ApiResponse appointmentBookResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("AppointmentBookResponse").toString())))
        ).description("Booking slot for the requestId");

        ApiResponse summaryResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("SummaryResponse").toString())))
        ).description("Order summary Response");

        ApiResponse customerReviewResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("CustomerReview").toString())))
        ).description("Customer Review Response");

        ApiResponse customerConfirmResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("customerConfirm").toString())))
        ).description("Customer Confirm Response");

        ApiResponse customerCancelResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("customerCancel").toString())))
        ).description("Customer Cancel Response");

        ApiResponse planResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("planResponse").toString())))
        ).description("plan Response");

        ApiResponse BadResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(json.read().get("badResponse").toString())))
        ).description("Customer Bad Response");


        Components components = new Components();
        components.addResponses("loginResponse", loginResponse);
        components.addResponses("loginFailureResponse", loginFailureResponse );
        components.addResponses("unauthenticatedResponse", unauthenticatedResponse);
        components.addResponses("CustomerResponse", CustomerResponse);
        components.addResponses("AppointmentResponse", AppointmentResponse);
        components.addResponses("verifyResponse", verifyResponse);
        components.addResponses("verifyFailureResponse",verifyFailureResponse);
        components.addResponses("notFoundResponse",notFoundResponse);
        components.addResponses("appointmentBookResponse",appointmentBookResponse);
        components.addResponses("summaryResponse",summaryResponse);
        components.addResponses("customerReviewResponse",customerReviewResponse);
        components.addResponses("customerConfirmResponse",customerConfirmResponse);
        components.addResponses("customerCancelResponse",customerCancelResponse);
        components.addResponses("planResponse",planResponse);
        components.addResponses("BadResponse ", BadResponse );

        return new OpenAPI()
                .info(new Info().title("Spring Document").version("1.0.0").description("Dealer Management Service"))
                .servers(List.of(new Server().url("/").description("Default Server URL")))
                .security(List.of(new SecurityRequirement().addList("Bearer-token")))
                .components(components);
    }


}

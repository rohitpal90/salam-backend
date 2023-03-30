package com.salam.dms.config.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@OpenAPIDefinition
@Configuration
@SecurityScheme(
        name = "Bearer-token",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerDocConfig {

    @Value("classpath:response.json")
    Resource exampleResource;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules()
            .registerModule(new SimpleModule());

    @Bean
    public OpenAPI baseOpenAPI() throws IOException {
        var responseJson = loadResponseJson();
        var components = new Components();

        responseJson.fields().forEachRemaining(entry -> {
            var key = entry.getKey();
            var examplesJson = responseJson.get(key);

            var example = new Example().value(examplesJson).$ref(key);
            components.addExamples(key, example);
            components.addResponses(key, createApiResponse(key, example));
        });

        return new OpenAPI()
                .info(new Info().title("Dealer Management Service").version("v1.0.0"))
                .servers(List.of(new Server().url("/").description("Default Server URL")))
                .security(List.of(new SecurityRequirement().addList("Bearer-token")))
                .components(components);
    }

    private JsonNode loadResponseJson() throws IOException {
        return OBJECT_MAPPER.readValue(exampleResource.getInputStream(), JsonNode.class);
    }

    private ApiResponse createApiResponse(String key, Example example) {
        var mediaType = new MediaType().addExamples(key, example);
        return new ApiResponse().content(new Content().addMediaType(APPLICATION_JSON_VALUE, mediaType));
    }
}

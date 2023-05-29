package com.salam.dms.config;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.salam.dms.model.request.AuthenticationRequestPOJOBuilder;
import com.salam.libs.annotations.EnableSalamWebClients;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

@Configuration
@ComponentScan("dev.samstevens.totp.spring.autoconfigure")
@EnableSalamWebClients
public class AppConfig {

    @Value("classpath:mocks/*")
    Resource[] resources;

    @Bean
    public Map<String, String> mocksMap() throws IOException {
        var mocksMap = new ConcurrentHashMap<String, String>();
        for (Resource resource : requireNonNull(resources)) {
            var name = removeFileExt(resource.getFilename(), true);
            var content = resource.getInputStream().readAllBytes();

            mocksMap.put(requireNonNull(name), new String(content, StandardCharsets.UTF_8));
        }

        return Collections.unmodifiableMap(mocksMap);
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .annotationIntrospector(new JacksonAnnotationIntrospector() {
                    @Override
                    public Class<?> findPOJOBuilder(AnnotatedClass ac) {
                        if (AuthenticationRequest.class.equals(ac.getRawType())) {
                            return AuthenticationRequestPOJOBuilder.class;
                        }

                        return super.findPOJOBuilder(ac);
                    }

                    @Override
                    public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac) {
                        if (ac.hasAnnotation(JsonPOJOBuilder.class)) {
                            return super.findPOJOBuilderConfig(ac);
                        }

                        return new JsonPOJOBuilder.Value("build", "");
                    }
                });
    }

    private static String removeFileExt(String filename, boolean removeAllExtensions) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
        return filename.replaceAll(extPattern, "");
    }

}

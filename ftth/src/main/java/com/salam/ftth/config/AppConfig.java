package com.salam.ftth.config;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.salam.ftth.db.entity.User;
import com.salam.ftth.model.request.AuthenticationRequestPOJOBuilder;
import com.salam.libs.annotations.EnableSalamWorkflow;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationRequest;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

@Configuration
@EnableSalamWorkflow
@EnableFeignClients
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

    @Bean
    public User wfUser(EntityManager em, @Value("${app.wf.user}") String ftthAppUser) {
        return em.createQuery("from User u join fetch u.roles where u.email = :email", User.class)
                .setParameter("email", ftthAppUser)
                .getResultList().get(0);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:ValidationMessages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    private static String removeFileExt(String filename, boolean removeAllExtensions) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
        return filename.replaceAll(extPattern, "");
    }
}

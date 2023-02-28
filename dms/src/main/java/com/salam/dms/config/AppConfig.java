package com.salam.dms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

@Configuration
@ComponentScan("dev.samstevens.totp.spring.autoconfigure")
@EnableFeignClients
public class AppConfig {

    @Value("classpath:mocks/*")
    Resource[] resources;

    @Bean
    public Map<String, String> mocksMap() throws IOException {
        var mocksMap = new ConcurrentHashMap<String, String>();
        for (Resource resource: requireNonNull(resources)) {
            var name = removeFileExt(resource.getFilename(), true);
            var content = resource.getInputStream().readAllBytes();

            mocksMap.put(requireNonNull(name), new String(content, StandardCharsets.UTF_8));
        }

        return Collections.unmodifiableMap(mocksMap);
    }

    private static String removeFileExt(String filename, boolean removeAllExtensions) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
        return filename.replaceAll(extPattern, "");
    }

}

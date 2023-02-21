package com.salam.libs.sm.config;

import com.salam.libs.common.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:workflow.yml", factory = YamlPropertySourceFactory.class)
public class WorkflowProperties {
    private String name;
    private String initialState;
    private List<Transition> transitions;


    @Data
    static class Transition {
        private String source;
        private String target;
        private String event;
    }

    public Set<String> getStates() {
        return transitions
                .stream()
                .flatMap(transition -> Stream.of(transition.getSource(), transition.getTarget()))
                .collect(Collectors.toSet());
    }

}

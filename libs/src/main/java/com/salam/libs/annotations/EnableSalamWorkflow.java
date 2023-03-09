package com.salam.libs.annotations;

import com.salam.libs.sm.config.StateMachineAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(StateMachineAutoConfiguration.class)
public @interface EnableSalamWorkflow {
}

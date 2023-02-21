package com.salam.libs.annotations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.statemachine.config.EnableStateMachineFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ComponentScan("com.salam.libs.sm")
@EntityScan(basePackages = "com.salam.libs.sm.entity")
public @interface EnableSalamWorkflow {
}

package com.salam.libs.sm;

import com.salam.libs.sm.config.EnableSalamWorkflow;
import com.salam.libs.sm.config.StateMachineAdapter;
import com.salam.libs.sm.entity.Request;
import com.salam.libs.sm.model.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSalamWorkflow
@SpringBootApplication
public class WorkflowApp implements CommandLineRunner {

    class OrderContext extends RequestContext {
        public OrderContext(Request request) {
            super(request);
        }

    }

    @Autowired
    StateMachineAdapter stateMachineAdapter;

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        stateMachineAdapter.trigger("TEST", null);
    }
}

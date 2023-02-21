package com.salam.libs.main;

import com.salam.libs.annotations.EnableSalamWorkflow;
import com.salam.libs.sm.config.StateMachineAdapter;
import com.salam.libs.sm.entity.Request;
import com.salam.libs.sm.model.EventResult;
import com.salam.libs.sm.model.RequestContext;
import com.salam.libs.sm.model.TestOrderRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSalamWorkflow
@SpringBootApplication
public class WorkflowApp implements CommandLineRunner {

    static class OrderContext extends RequestContext {

        @Setter @Getter
        private TestOrderRequest testOrderRequest;


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
        var request = new Request();
        request.setId(1L);
        request.setState("TEST1");
        request.setOrderId("ORD00001");

        var orderContext = new OrderContext(request);
        EventResult result = stateMachineAdapter.trigger("EVENT1", orderContext).block();
        System.out.println(result);
    }
}

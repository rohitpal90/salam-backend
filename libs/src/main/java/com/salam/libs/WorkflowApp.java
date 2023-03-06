package com.salam.libs;

import com.fasterxml.jackson.databind.JsonNode;
import com.salam.libs.annotations.EnableSalamWorkflow;
import com.salam.libs.sm.config.GuardHandler;
import com.salam.libs.sm.config.StateMachineAdapter;
import com.salam.libs.sm.model.RequestContext;
import com.salam.libs.sm.model.TestOrderRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@EnableSalamWorkflow
@SpringBootApplication
public class WorkflowApp implements CommandLineRunner {

    @Slf4j
    @Component
    static public class EventOneGuard extends GuardHandler {

        @Override
        public void handle(StateContext<String, String> context) {
            var requestContext = RequestContext.fromStateMachine(context.getStateMachine());
            log.info("guard test: {}",requestContext);
        }

        @Override
        public String forType() {
            return "EVENT1";
        }
    }


    @Setter @Getter
    static class OrderContext extends RequestContext {

        private TestOrderRequest testOrderRequest;
        private MyMeta metaInfo;


        public OrderContext(String orderId, MyMeta metaInfo) {
            super(orderId);
            this.metaInfo = metaInfo;
        }

        public OrderContext(String orderId) {
            super(orderId);
        }

        @Override
        public JsonNode getMeta() {
            return getMapper().convertValue(metaInfo, JsonNode.class);
        }
    }

    @Data
    static class MyMeta {
        private String value;
    }

    @Autowired
    StateMachineAdapter stateMachineAdapter;


    public static void main(String[] args) {
        SpringApplication.run(WorkflowApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String newOrderId = "ORD0004";
        var meta = new MyMeta();
        meta.setValue("TEST");

        var requestContext = new OrderContext(newOrderId);
        requestContext.setMetaInfo(meta);

        stateMachineAdapter.create(requestContext);
        var eventResult = stateMachineAdapter.trigger("EVENT1", requestContext).block();
    }
}

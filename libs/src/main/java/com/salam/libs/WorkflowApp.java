package com.salam.libs;

import com.salam.libs.annotations.EnableSalamWebClients;
import com.salam.libs.annotations.EnableSalamWorkflow;
import com.salam.libs.feign.elm.client.AbsherClient;
import com.salam.libs.feign.elm.model.SendOtpRequest;
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
@EnableSalamWebClients
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


    @Setter
    @Getter
    static class OrderContext extends RequestContext<MyMeta> {

        private TestOrderRequest testOrderRequest;

        public OrderContext(String orderId, Long userId) {
            super(orderId, userId);
        }

        @Override
        public Class<MyMeta> getMetaClass() {
            return MyMeta.class;
        }
    }

    @Data
    static class MyMeta {
        private String value;
    }

    @Autowired
    StateMachineAdapter stateMachineAdapter;

    @Autowired
    AbsherClient absherClient;


    public static void main(String[] args) {
        SpringApplication.run(WorkflowApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        testAbsherClient();
    }

    void testStateMachine() {
        String newOrderId = "ORD0004";
        var meta = new MyMeta();
        meta.setValue("TEST");

        var requestContext = new OrderContext(newOrderId, 1L);
        requestContext.setMeta(meta);

        stateMachineAdapter.create(requestContext);
        var eventResult = stateMachineAdapter.trigger("EVENT1", requestContext).block();
    }

    void testAbsherClient() {
        System.out.println(absherClient.sendOtpRequest(
                SendOtpRequest.builder()
                        .customerId("customerId")
                        .packageName("packageName")
                        .language("en")
                        .reason("reason")
                        .operatorId("operatorId")
                        .build()
        ));
    }
}

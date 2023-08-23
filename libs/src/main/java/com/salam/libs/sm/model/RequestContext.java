package com.salam.libs.sm.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import org.springframework.statemachine.StateMachine;

public abstract class RequestContext<M> {
    private Long requestId;
    private Long userId;
    private String orderId;
    private Exception currentError;
    private M meta;
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = (new ObjectMapper()).findAndRegisterModules().registerModule(new SimpleModule());
    }

    public static final String KEY = "requestContext";
    public static final String ERROR_KEY = "error";

    public RequestContext(String orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

    public static <T extends RequestContext> T fromStateMachine(StateMachine<String, String> stateMachine) {
        var variables = stateMachine.getExtendedState().getVariables();
        var reqContext = variables.get(KEY);

        if (variables.containsKey(ERROR_KEY)) {
            ((RequestContext) reqContext).setCurrentError((Exception) variables.get(ERROR_KEY));
        }

        return ((T) reqContext);
    }

    public StateMachine<String, String> setToStateMachineState(StateMachine<String, String> stateMachine) {
        stateMachine.getExtendedState().getVariables().put(KEY, this);
        return stateMachine;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Exception getCurrentError() {
        return currentError;
    }

    public void setCurrentError(Exception currentError) {
        this.currentError = currentError;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public M getMeta() {
        return meta;
    }

    public void setMeta(M meta) {
        this.meta = meta;
    }

    @SneakyThrows
    public void setMetaRaw(JsonNode metaRaw) {
        this.setMeta(
                objectMapper.treeToValue(metaRaw, getMetaClass())
        );
    }

    public JsonNode getMetaRaw() {
        return objectMapper.convertValue(getMeta(), JsonNode.class);
    }

    public abstract Class<M> getMetaClass();
}

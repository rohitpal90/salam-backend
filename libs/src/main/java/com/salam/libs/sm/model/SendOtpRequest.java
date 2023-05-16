package com.salam.libs.sm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;



@Data
public class SendOtpRequest {
  @JsonProperty("customerId")
  private String customerId;

  @JsonProperty("operatorId")
  private String operatorId;

  @JsonProperty("reason")
  private String reason ;

  @JsonProperty("packageName")
  private String packageName;

  @JsonProperty("language")
  private String language;


}

package com.salam.libs.sm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class SendOtpResponse {
  @JsonProperty("verificationCode")
  private String verificationCode ;

}

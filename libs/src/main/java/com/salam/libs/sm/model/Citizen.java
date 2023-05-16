package com.salam.libs.sm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Citizen {
  @JsonProperty("dateOfBirthH")
  private String dateOfBirthH ;

  @JsonProperty("englishFirstName")
  private String englishFirstName;

  @JsonProperty("englishLastName")
  private String englishLastName ;

  @JsonProperty("englishSecondName")
  private String englishSecondName ;

  @JsonProperty("englishThirdName")
  private String englishThirdName;

  @JsonProperty("familyName")
  private String familyName ;

  @JsonProperty("fatherName")
  private String fatherName ;

  @JsonProperty("firstName")
  private String firstName ;

  @JsonProperty("grandFatherName")
  private String grandFatherName;

  @JsonProperty("idExpiryDate")
  private String idExpiryDate ;


}

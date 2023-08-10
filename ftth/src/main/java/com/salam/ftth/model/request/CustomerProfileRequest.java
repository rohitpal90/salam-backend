package com.salam.ftth.model.request;


import lombok.Data;

@Data
public class CustomerProfileRequest {
    private IDType idType;
    private String id;
    private String mobile;
    private String dob;
    private String email;
    private String fullName;
    private String username;
    private String password;
}

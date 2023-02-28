package com.salam.dms.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class User {

    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    private String totp;
    private String phoneNo;
    private boolean active;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}

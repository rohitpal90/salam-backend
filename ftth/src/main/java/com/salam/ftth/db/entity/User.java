package com.salam.ftth.db.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Data
public class User {

    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    private String totp;
    private String phone;
    private boolean active;

    @OneToMany
    @JoinTable(
            name="user_role",
            joinColumns = @JoinColumn( name="user_id"),
            inverseJoinColumns = @JoinColumn( name="role_id")
    )
    private Set<Role> roles;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}

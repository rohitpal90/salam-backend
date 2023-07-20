package com.salam.ftth.db.entity;

import com.salam.ftth.model.UserMetaInfo;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.Date;
import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private UserMetaInfo meta;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    @PrePersist
    public void prePersist() {
        var now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }
}

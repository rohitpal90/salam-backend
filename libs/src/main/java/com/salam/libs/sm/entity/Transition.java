package com.salam.libs.sm.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Builder
public class Transition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "`from`")
    private String from;

    @Column(name = "`to`")
    private String to;

    private Long requestId;

    @Column(columnDefinition = "json")
    private String payload;

    private Date createdAt;

    @PrePersist
    public void perPersist() {
        this.createdAt = new Date();
    }

}

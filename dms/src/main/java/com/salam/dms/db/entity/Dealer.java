package com.salam.dms.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.Auditable;

import java.util.Date;


@Data
@Entity
@Table(name = "dealer")
public class Dealer {
    @Id
    private Long id;
    private String name;
    private String phone;
    private String totp;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}

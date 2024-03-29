package com.salam.dms.db.entity;

import com.salam.dms.model.RequestMetaInfo;
import com.salam.dms.model.States;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private States state;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private RequestMetaInfo meta;

    @Column(updatable = false)
    private Long userId;

    @Column(updatable = false)
    private String orderId;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private Date finishedAt;

    @PrePersist
    public void prePersist() {
        var now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        var now = new Date();
        if (States.terminalStates().contains(this.state)) {
            this.finishedAt = now;
        }

        this.updatedAt = now;
    }

}

package com.salam.dms.db.entity;

import com.salam.dms.model.Event;
import com.salam.dms.model.RequestMetaInfo;
import com.salam.dms.model.States;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.Date;

@Data
@Entity
@Builder
public class Transition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Event name;

    @Column(name = "`from`")
    @Enumerated(EnumType.STRING)
    private States from;

    @Column(name = "`to`")
    @Enumerated(EnumType.STRING)
    private States to;

    private Long requestId;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private RequestMetaInfo payload;

    private Date createdAt;

    @PrePersist
    public void perPersist() {
        this.createdAt = new Date();
    }

}

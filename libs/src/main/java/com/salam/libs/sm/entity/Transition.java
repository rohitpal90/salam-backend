package com.salam.libs.sm.entity;

import com.fasterxml.jackson.databind.JsonNode;
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

    private String name;

    @Column(name = "`from`")
    private String from;

    @Column(name = "`to`")
    private String to;

    private Long requestId;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private JsonNode payload;

    private Date createdAt;

    @PrePersist
    public void perPersist() {
        this.createdAt = new Date();
    }

}

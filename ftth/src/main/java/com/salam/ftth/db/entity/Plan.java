package com.salam.ftth.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salam.ftth.adapter.model.PlanMetaInfo;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

@Data
@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @JsonProperty("planId")
    private Long id;

    private String openAccessId;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private PlanMetaInfo meta;
}

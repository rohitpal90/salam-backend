package com.salam.dms.db.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class DealerPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dealerId;
    private Long planId;
}

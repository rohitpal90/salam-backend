package com.salam.ftth.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class PlanInfo {
    private @NonNull String planId;
    private @NonNull String provider;
}

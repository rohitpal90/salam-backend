package com.salam.dms.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class PlanInfo {
    private @NonNull String planId;
    private @NonNull String provider;
}

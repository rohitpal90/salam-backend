package com.salam.ftth.adapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    public String taskType;
    public String appointmentDatetime;
    public String teamId;
    public String slotId;
}

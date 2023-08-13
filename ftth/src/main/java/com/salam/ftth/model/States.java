package com.salam.ftth.model;

import java.util.List;

public enum States {
    ACCOUNT_CREATION,
    MOBILE_VERIFICATION,
    ACCOUNT_CREATED,
    INSTALLATION_SCHEDULE,
    REQUEST_CONFIRMATION,
    INSTALLATION,
    REJECTED,
    CANCELLED,
    REVIEW,
    COMPLETED;

    public static List<States> terminalStates() {
        return List.of(CANCELLED, COMPLETED, REJECTED);
    }

}

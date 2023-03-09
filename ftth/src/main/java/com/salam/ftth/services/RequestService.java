package com.salam.ftth.services;

import com.salam.ftth.db.entity.User;
import com.salam.ftth.model.PlanInfo;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.RequestMetaInfo;
import com.salam.ftth.model.request.CustomerProfileRequest;
import com.salam.libs.sm.config.StateMachineAdapter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@AllArgsConstructor
@Service
public class RequestService {

    private final StateMachineAdapter stateMachineAdapter;

}

package com.salam.dms.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.dms.adapter.feign.client.AppointmentClient;
import com.salam.dms.adapter.feign.mock.ClientMockAdapter;
import com.salam.dms.adapter.model.Appointment;
import com.salam.dms.adapter.model.request.AppointmentRequest;
import com.salam.dms.adapter.model.response.AppointmentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class AppointmentServiceTest {

    @Mock AppointmentClient appointmentClient;
    @Mock ClientMockAdapter clientMockAdapter;
    @InjectMocks AppointmentService appointmentService;
    @Value("classpath:data/appointments.json")
    Resource appointmentsResource;

    private static List<Appointment> appointments;
    private static AppointmentRequest request;

    @BeforeEach
    void setUp() {
        // base setup
        request = createAppointmentRequest();
        appointments = loadMockAppointments();

        // setup client mock
        var response = new AppointmentResponse();
        response.setAppointmentList(appointments);
        when(appointmentClient.fetchAppointmentSlots(request)).thenReturn(response);
        when(clientMockAdapter.getFor(Mockito.any(), Mockito.any())).thenReturn(response);
    }

    private List<Appointment> loadMockAppointments() {
        try {
            var mapper = new ObjectMapper();
            return mapper.readValue(appointmentsResource.getFile(), new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static AppointmentRequest createAppointmentRequest() {
        var request = new AppointmentRequest();
        request.setAccessMode("GPON");
        request.setStartDate("2022-11-11");
        request.setSubsPlanId("4003");
        return request;
    }

    @Test
    void fetchAppointments() {
        assertEquals(appointmentService.fetchAppointments(request).size(), 32);
    }

    @Test
    void findAvailableAppointments() throws Exception {
        List<Appointment> availableAppointments = appointmentService.findAvailableAppointments("17/09/2022",
                "12:00 - 15:00", request);
        assertEquals(availableAppointments.size(), 1);

        Appointment appointment = availableAppointments.get(0);
        assertEquals(appointment.getSlotId(), "SLOT123");
    }
}
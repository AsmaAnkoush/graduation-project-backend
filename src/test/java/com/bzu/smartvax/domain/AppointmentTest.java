package com.bzu.smartvax.domain;

import static com.bzu.smartvax.domain.AppointmentTestSamples.*;
import static com.bzu.smartvax.domain.ChildTestSamples.*;
import static com.bzu.smartvax.domain.HealthWorkerTestSamples.*;
import static com.bzu.smartvax.domain.ParentTestSamples.*;
import static com.bzu.smartvax.domain.ScheduleVaccinationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointment.class);
        Appointment appointment1 = getAppointmentSample1();
        Appointment appointment2 = new Appointment();
        assertThat(appointment1).isNotEqualTo(appointment2);

        appointment2.setId(appointment1.getId());
        assertThat(appointment1).isEqualTo(appointment2);

        appointment2 = getAppointmentSample2();
        assertThat(appointment1).isNotEqualTo(appointment2);
    }

    @Test
    void parentTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        Parent parentBack = getParentRandomSampleGenerator();

        appointment.setParent(parentBack);
        assertThat(appointment.getParent()).isEqualTo(parentBack);

        appointment.parent(null);
        assertThat(appointment.getParent()).isNull();
    }

    @Test
    void childTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        Child childBack = getChildRandomSampleGenerator();

        appointment.setChild(childBack);
        assertThat(appointment.getChild()).isEqualTo(childBack);

        appointment.child(null);
        assertThat(appointment.getChild()).isNull();
    }

    @Test
    void scheduleTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        ScheduleVaccination scheduleVaccinationBack = getScheduleVaccinationRandomSampleGenerator();

        appointment.setSchedule(scheduleVaccinationBack);
        assertThat(appointment.getSchedule()).isEqualTo(scheduleVaccinationBack);

        appointment.schedule(null);
        assertThat(appointment.getSchedule()).isNull();
    }

    @Test
    void healthWorkerTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        HealthWorker healthWorkerBack = getHealthWorkerRandomSampleGenerator();

        appointment.setHealthWorker(healthWorkerBack);
        assertThat(appointment.getHealthWorker()).isEqualTo(healthWorkerBack);

        appointment.healthWorker(null);
        assertThat(appointment.getHealthWorker()).isNull();
    }
}

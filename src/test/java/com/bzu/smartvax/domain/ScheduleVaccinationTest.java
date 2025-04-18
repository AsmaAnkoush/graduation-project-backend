package com.bzu.smartvax.domain;

import static com.bzu.smartvax.domain.AppointmentTestSamples.*;
import static com.bzu.smartvax.domain.ChildTestSamples.*;
import static com.bzu.smartvax.domain.ScheduleVaccinationTestSamples.*;
import static com.bzu.smartvax.domain.VaccinationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ScheduleVaccinationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleVaccination.class);
        ScheduleVaccination scheduleVaccination1 = getScheduleVaccinationSample1();
        ScheduleVaccination scheduleVaccination2 = new ScheduleVaccination();
        assertThat(scheduleVaccination1).isNotEqualTo(scheduleVaccination2);

        scheduleVaccination2.setId(scheduleVaccination1.getId());
        assertThat(scheduleVaccination1).isEqualTo(scheduleVaccination2);

        scheduleVaccination2 = getScheduleVaccinationSample2();
        assertThat(scheduleVaccination1).isNotEqualTo(scheduleVaccination2);
    }

    @Test
    void appointmentsTest() {
        ScheduleVaccination scheduleVaccination = getScheduleVaccinationRandomSampleGenerator();
        Appointment appointmentBack = getAppointmentRandomSampleGenerator();

        scheduleVaccination.addAppointments(appointmentBack);
        assertThat(scheduleVaccination.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getSchedule()).isEqualTo(scheduleVaccination);

        scheduleVaccination.removeAppointments(appointmentBack);
        assertThat(scheduleVaccination.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getSchedule()).isNull();

        scheduleVaccination.appointments(new HashSet<>(Set.of(appointmentBack)));
        assertThat(scheduleVaccination.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getSchedule()).isEqualTo(scheduleVaccination);

        scheduleVaccination.setAppointments(new HashSet<>());
        assertThat(scheduleVaccination.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getSchedule()).isNull();
    }

    @Test
    void childTest() {
        ScheduleVaccination scheduleVaccination = getScheduleVaccinationRandomSampleGenerator();
        Child childBack = getChildRandomSampleGenerator();

        scheduleVaccination.setChild(childBack);
        assertThat(scheduleVaccination.getChild()).isEqualTo(childBack);

        scheduleVaccination.child(null);
        assertThat(scheduleVaccination.getChild()).isNull();
    }

    @Test
    void vaccinationTest() {
        ScheduleVaccination scheduleVaccination = getScheduleVaccinationRandomSampleGenerator();
        Vaccination vaccinationBack = getVaccinationRandomSampleGenerator();

        scheduleVaccination.setVaccination(vaccinationBack);
        assertThat(scheduleVaccination.getVaccination()).isEqualTo(vaccinationBack);

        scheduleVaccination.vaccination(null);
        assertThat(scheduleVaccination.getVaccination()).isNull();
    }
}

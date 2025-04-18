package com.bzu.smartvax.domain;

import static com.bzu.smartvax.domain.AppointmentTestSamples.*;
import static com.bzu.smartvax.domain.ChildTestSamples.*;
import static com.bzu.smartvax.domain.HealthRecordTestSamples.*;
import static com.bzu.smartvax.domain.ParentTestSamples.*;
import static com.bzu.smartvax.domain.ScheduleVaccinationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChildTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Child.class);
        Child child1 = getChildSample1();
        Child child2 = new Child();
        assertThat(child1).isNotEqualTo(child2);

        child2.setId(child1.getId());
        assertThat(child1).isEqualTo(child2);

        child2 = getChildSample2();
        assertThat(child1).isNotEqualTo(child2);
    }

    @Test
    void healthRecordTest() {
        Child child = getChildRandomSampleGenerator();
        HealthRecord healthRecordBack = getHealthRecordRandomSampleGenerator();

        child.setHealthRecord(healthRecordBack);
        assertThat(child.getHealthRecord()).isEqualTo(healthRecordBack);

        child.healthRecord(null);
        assertThat(child.getHealthRecord()).isNull();
    }

    @Test
    void appointmentsTest() {
        Child child = getChildRandomSampleGenerator();
        Appointment appointmentBack = getAppointmentRandomSampleGenerator();

        child.addAppointments(appointmentBack);
        assertThat(child.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getChild()).isEqualTo(child);

        child.removeAppointments(appointmentBack);
        assertThat(child.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getChild()).isNull();

        child.appointments(new HashSet<>(Set.of(appointmentBack)));
        assertThat(child.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getChild()).isEqualTo(child);

        child.setAppointments(new HashSet<>());
        assertThat(child.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getChild()).isNull();
    }

    @Test
    void scheduleVaccinationsTest() {
        Child child = getChildRandomSampleGenerator();
        ScheduleVaccination scheduleVaccinationBack = getScheduleVaccinationRandomSampleGenerator();

        child.addScheduleVaccinations(scheduleVaccinationBack);
        assertThat(child.getScheduleVaccinations()).containsOnly(scheduleVaccinationBack);
        assertThat(scheduleVaccinationBack.getChild()).isEqualTo(child);

        child.removeScheduleVaccinations(scheduleVaccinationBack);
        assertThat(child.getScheduleVaccinations()).doesNotContain(scheduleVaccinationBack);
        assertThat(scheduleVaccinationBack.getChild()).isNull();

        child.scheduleVaccinations(new HashSet<>(Set.of(scheduleVaccinationBack)));
        assertThat(child.getScheduleVaccinations()).containsOnly(scheduleVaccinationBack);
        assertThat(scheduleVaccinationBack.getChild()).isEqualTo(child);

        child.setScheduleVaccinations(new HashSet<>());
        assertThat(child.getScheduleVaccinations()).doesNotContain(scheduleVaccinationBack);
        assertThat(scheduleVaccinationBack.getChild()).isNull();
    }

    @Test
    void parentTest() {
        Child child = getChildRandomSampleGenerator();
        Parent parentBack = getParentRandomSampleGenerator();

        child.setParent(parentBack);
        assertThat(child.getParent()).isEqualTo(parentBack);

        child.parent(null);
        assertThat(child.getParent()).isNull();
    }
}

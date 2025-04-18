package com.bzu.smartvax.domain;

import static com.bzu.smartvax.domain.AppointmentTestSamples.*;
import static com.bzu.smartvax.domain.HealthWorkerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HealthWorkerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HealthWorker.class);
        HealthWorker healthWorker1 = getHealthWorkerSample1();
        HealthWorker healthWorker2 = new HealthWorker();
        assertThat(healthWorker1).isNotEqualTo(healthWorker2);

        healthWorker2.setId(healthWorker1.getId());
        assertThat(healthWorker1).isEqualTo(healthWorker2);

        healthWorker2 = getHealthWorkerSample2();
        assertThat(healthWorker1).isNotEqualTo(healthWorker2);
    }

    @Test
    void appointmentsTest() {
        HealthWorker healthWorker = getHealthWorkerRandomSampleGenerator();
        Appointment appointmentBack = getAppointmentRandomSampleGenerator();

        healthWorker.addAppointments(appointmentBack);
        assertThat(healthWorker.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getHealthWorker()).isEqualTo(healthWorker);

        healthWorker.removeAppointments(appointmentBack);
        assertThat(healthWorker.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getHealthWorker()).isNull();

        healthWorker.appointments(new HashSet<>(Set.of(appointmentBack)));
        assertThat(healthWorker.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getHealthWorker()).isEqualTo(healthWorker);

        healthWorker.setAppointments(new HashSet<>());
        assertThat(healthWorker.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getHealthWorker()).isNull();
    }
}

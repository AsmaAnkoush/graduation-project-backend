package com.bzu.smartvax.domain;

import static com.bzu.smartvax.domain.AppointmentTestSamples.*;
import static com.bzu.smartvax.domain.ParentTestSamples.*;
import static com.bzu.smartvax.domain.ReminderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReminderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reminder.class);
        Reminder reminder1 = getReminderSample1();
        Reminder reminder2 = new Reminder();
        assertThat(reminder1).isNotEqualTo(reminder2);

        reminder2.setId(reminder1.getId());
        assertThat(reminder1).isEqualTo(reminder2);

        reminder2 = getReminderSample2();
        assertThat(reminder1).isNotEqualTo(reminder2);
    }

    @Test
    void appointmentTest() {
        Reminder reminder = getReminderRandomSampleGenerator();
        Appointment appointmentBack = getAppointmentRandomSampleGenerator();

        reminder.setAppointment(appointmentBack);
        assertThat(reminder.getAppointment()).isEqualTo(appointmentBack);

        reminder.setAppointment(null);
        assertThat(reminder.getAppointment()).isNull();
    }

    @Test
    void recipientTest() {
        Reminder reminder = getReminderRandomSampleGenerator();
        Parent parentBack = getParentRandomSampleGenerator();

        reminder.setRecipient(parentBack);
        assertThat(reminder.getRecipient()).isEqualTo(parentBack);

        reminder.setRecipient(null);
        assertThat(reminder.getRecipient()).isNull();
    }
}

package com.bzu.smartvax.domain;

import static com.bzu.smartvax.domain.AppointmentTestSamples.*;
import static com.bzu.smartvax.domain.ChildTestSamples.*;
import static com.bzu.smartvax.domain.FeedbackTestSamples.*;
import static com.bzu.smartvax.domain.ParentTestSamples.*;
import static com.bzu.smartvax.domain.ReminderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ParentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parent.class);
        Parent parent1 = getParentSample1();
        Parent parent2 = new Parent();
        assertThat(parent1).isNotEqualTo(parent2);

        parent2.setId(parent1.getId());
        assertThat(parent1).isEqualTo(parent2);

        parent2 = getParentSample2();
        assertThat(parent1).isNotEqualTo(parent2);
    }

    @Test
    void childrenTest() {
        Parent parent = getParentRandomSampleGenerator();
        Child childBack = getChildRandomSampleGenerator();

        parent.addChildren(childBack);
        assertThat(parent.getChildren()).containsOnly(childBack);
        assertThat(childBack.getParent()).isEqualTo(parent);

        parent.removeChildren(childBack);
        assertThat(parent.getChildren()).doesNotContain(childBack);
        assertThat(childBack.getParent()).isNull();

        parent.children(new HashSet<>(Set.of(childBack)));
        assertThat(parent.getChildren()).containsOnly(childBack);
        assertThat(childBack.getParent()).isEqualTo(parent);

        parent.setChildren(new HashSet<>());
        assertThat(parent.getChildren()).doesNotContain(childBack);
        assertThat(childBack.getParent()).isNull();
    }

    @Test
    void appointmentsTest() {
        Parent parent = getParentRandomSampleGenerator();
        Appointment appointmentBack = getAppointmentRandomSampleGenerator();

        parent.addAppointments(appointmentBack);
        assertThat(parent.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getParent()).isEqualTo(parent);

        parent.removeAppointments(appointmentBack);
        assertThat(parent.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getParent()).isNull();

        parent.appointments(new HashSet<>(Set.of(appointmentBack)));
        assertThat(parent.getAppointments()).containsOnly(appointmentBack);
        assertThat(appointmentBack.getParent()).isEqualTo(parent);

        parent.setAppointments(new HashSet<>());
        assertThat(parent.getAppointments()).doesNotContain(appointmentBack);
        assertThat(appointmentBack.getParent()).isNull();
    }

    @Test
    void remindersTest() {
        Parent parent = getParentRandomSampleGenerator();
        Reminder reminderBack = getReminderRandomSampleGenerator();

        parent.addReminders(reminderBack);
        assertThat(parent.getReminders()).containsOnly(reminderBack);
        assertThat(reminderBack.getRecipient()).isEqualTo(parent);

        parent.removeReminders(reminderBack);
        assertThat(parent.getReminders()).doesNotContain(reminderBack);
        assertThat(reminderBack.getRecipient()).isNull();

        parent.reminders(new HashSet<>(Set.of(reminderBack)));
        assertThat(parent.getReminders()).containsOnly(reminderBack);
        assertThat(reminderBack.getRecipient()).isEqualTo(parent);

        parent.setReminders(new HashSet<>());
        assertThat(parent.getReminders()).doesNotContain(reminderBack);
        assertThat(reminderBack.getRecipient()).isNull();
    }

    @Test
    void feedbacksTest() {
        Parent parent = getParentRandomSampleGenerator();
        Feedback feedbackBack = getFeedbackRandomSampleGenerator();

        parent.addFeedbacks(feedbackBack);
        assertThat(parent.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getParent()).isEqualTo(parent);

        parent.removeFeedbacks(feedbackBack);
        assertThat(parent.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getParent()).isNull();

        parent.feedbacks(new HashSet<>(Set.of(feedbackBack)));
        assertThat(parent.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getParent()).isEqualTo(parent);

        parent.setFeedbacks(new HashSet<>());
        assertThat(parent.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getParent()).isNull();
    }
}

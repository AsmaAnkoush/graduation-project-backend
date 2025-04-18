package com.bzu.smartvax.domain;

import static com.bzu.smartvax.domain.FeedbackTestSamples.*;
import static com.bzu.smartvax.domain.ScheduleVaccinationTestSamples.*;
import static com.bzu.smartvax.domain.VaccinationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VaccinationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vaccination.class);
        Vaccination vaccination1 = getVaccinationSample1();
        Vaccination vaccination2 = new Vaccination();
        assertThat(vaccination1).isNotEqualTo(vaccination2);

        vaccination2.setId(vaccination1.getId());
        assertThat(vaccination1).isEqualTo(vaccination2);

        vaccination2 = getVaccinationSample2();
        assertThat(vaccination1).isNotEqualTo(vaccination2);
    }

    @Test
    void feedbacksTest() {
        Vaccination vaccination = getVaccinationRandomSampleGenerator();
        Feedback feedbackBack = getFeedbackRandomSampleGenerator();

        vaccination.addFeedbacks(feedbackBack);
        assertThat(vaccination.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getVaccination()).isEqualTo(vaccination);

        vaccination.removeFeedbacks(feedbackBack);
        assertThat(vaccination.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getVaccination()).isNull();

        vaccination.feedbacks(new HashSet<>(Set.of(feedbackBack)));
        assertThat(vaccination.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getVaccination()).isEqualTo(vaccination);

        vaccination.setFeedbacks(new HashSet<>());
        assertThat(vaccination.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getVaccination()).isNull();
    }

    @Test
    void scheduleVaccinationsTest() {
        Vaccination vaccination = getVaccinationRandomSampleGenerator();
        ScheduleVaccination scheduleVaccinationBack = getScheduleVaccinationRandomSampleGenerator();

        vaccination.addScheduleVaccinations(scheduleVaccinationBack);
        assertThat(vaccination.getScheduleVaccinations()).containsOnly(scheduleVaccinationBack);
        assertThat(scheduleVaccinationBack.getVaccination()).isEqualTo(vaccination);

        vaccination.removeScheduleVaccinations(scheduleVaccinationBack);
        assertThat(vaccination.getScheduleVaccinations()).doesNotContain(scheduleVaccinationBack);
        assertThat(scheduleVaccinationBack.getVaccination()).isNull();

        vaccination.scheduleVaccinations(new HashSet<>(Set.of(scheduleVaccinationBack)));
        assertThat(vaccination.getScheduleVaccinations()).containsOnly(scheduleVaccinationBack);
        assertThat(scheduleVaccinationBack.getVaccination()).isEqualTo(vaccination);

        vaccination.setScheduleVaccinations(new HashSet<>());
        assertThat(vaccination.getScheduleVaccinations()).doesNotContain(scheduleVaccinationBack);
        assertThat(scheduleVaccinationBack.getVaccination()).isNull();
    }
}

package com.bzu.smartvax.domain;

import static com.bzu.smartvax.domain.FeedbackTestSamples.*;
import static com.bzu.smartvax.domain.ParentTestSamples.*;
import static com.bzu.smartvax.domain.VaccinationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feedback.class);
        Feedback feedback1 = getFeedbackSample1();
        Feedback feedback2 = new Feedback();
        assertThat(feedback1).isNotEqualTo(feedback2);

        feedback2.setId(feedback1.getId());
        assertThat(feedback1).isEqualTo(feedback2);

        feedback2 = getFeedbackSample2();
        assertThat(feedback1).isNotEqualTo(feedback2);
    }

    @Test
    void parentTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        Parent parentBack = getParentRandomSampleGenerator();

        feedback.setParent(parentBack);
        assertThat(feedback.getParent()).isEqualTo(parentBack);

        feedback.parent(null);
        assertThat(feedback.getParent()).isNull();
    }

    @Test
    void vaccinationTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        Vaccination vaccinationBack = getVaccinationRandomSampleGenerator();

        feedback.setVaccination(vaccinationBack);
        assertThat(feedback.getVaccination()).isEqualTo(vaccinationBack);

        feedback.vaccination(null);
        assertThat(feedback.getVaccination()).isNull();
    }
}

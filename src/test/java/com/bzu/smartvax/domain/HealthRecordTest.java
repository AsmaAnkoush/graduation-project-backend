package com.bzu.smartvax.domain;

import static com.bzu.smartvax.domain.ChildTestSamples.*;
import static com.bzu.smartvax.domain.HealthRecordTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HealthRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HealthRecord.class);
        HealthRecord healthRecord1 = getHealthRecordSample1();
        HealthRecord healthRecord2 = new HealthRecord();
        assertThat(healthRecord1).isNotEqualTo(healthRecord2);

        healthRecord2.setId(healthRecord1.getId());
        assertThat(healthRecord1).isEqualTo(healthRecord2);

        healthRecord2 = getHealthRecordSample2();
        assertThat(healthRecord1).isNotEqualTo(healthRecord2);
    }

    @Test
    void childTest() {
        HealthRecord healthRecord = getHealthRecordRandomSampleGenerator();
        Child childBack = getChildRandomSampleGenerator();

        healthRecord.setChild(childBack);
        assertThat(healthRecord.getChild()).isEqualTo(childBack);
        assertThat(childBack.getHealthRecord()).isEqualTo(healthRecord);

        healthRecord.child(null);
        assertThat(healthRecord.getChild()).isNull();
        assertThat(childBack.getHealthRecord()).isNull();
    }
}

package com.bzu.smartvax.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HealthWorkerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HealthWorkerDTO.class);
        HealthWorkerDTO healthWorkerDTO1 = new HealthWorkerDTO();
        healthWorkerDTO1.setId(1L);
        HealthWorkerDTO healthWorkerDTO2 = new HealthWorkerDTO();
        assertThat(healthWorkerDTO1).isNotEqualTo(healthWorkerDTO2);
        healthWorkerDTO2.setId(healthWorkerDTO1.getId());
        assertThat(healthWorkerDTO1).isEqualTo(healthWorkerDTO2);
        healthWorkerDTO2.setId(2L);
        assertThat(healthWorkerDTO1).isNotEqualTo(healthWorkerDTO2);
        healthWorkerDTO1.setId(null);
        assertThat(healthWorkerDTO1).isNotEqualTo(healthWorkerDTO2);
    }
}

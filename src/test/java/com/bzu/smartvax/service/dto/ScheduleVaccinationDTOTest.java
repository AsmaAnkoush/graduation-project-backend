package com.bzu.smartvax.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduleVaccinationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleVaccinationDTO.class);
        ScheduleVaccinationDTO scheduleVaccinationDTO1 = new ScheduleVaccinationDTO();
        scheduleVaccinationDTO1.setId(1L);
        ScheduleVaccinationDTO scheduleVaccinationDTO2 = new ScheduleVaccinationDTO();
        assertThat(scheduleVaccinationDTO1).isNotEqualTo(scheduleVaccinationDTO2);
        scheduleVaccinationDTO2.setId(scheduleVaccinationDTO1.getId());
        assertThat(scheduleVaccinationDTO1).isEqualTo(scheduleVaccinationDTO2);
        scheduleVaccinationDTO2.setId(2L);
        assertThat(scheduleVaccinationDTO1).isNotEqualTo(scheduleVaccinationDTO2);
        scheduleVaccinationDTO1.setId(null);
        assertThat(scheduleVaccinationDTO1).isNotEqualTo(scheduleVaccinationDTO2);
    }
}

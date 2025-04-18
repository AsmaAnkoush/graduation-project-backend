package com.bzu.smartvax.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AIAnalyzerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AIAnalyzerDTO.class);
        AIAnalyzerDTO aIAnalyzerDTO1 = new AIAnalyzerDTO();
        aIAnalyzerDTO1.setId(1L);
        AIAnalyzerDTO aIAnalyzerDTO2 = new AIAnalyzerDTO();
        assertThat(aIAnalyzerDTO1).isNotEqualTo(aIAnalyzerDTO2);
        aIAnalyzerDTO2.setId(aIAnalyzerDTO1.getId());
        assertThat(aIAnalyzerDTO1).isEqualTo(aIAnalyzerDTO2);
        aIAnalyzerDTO2.setId(2L);
        assertThat(aIAnalyzerDTO1).isNotEqualTo(aIAnalyzerDTO2);
        aIAnalyzerDTO1.setId(null);
        assertThat(aIAnalyzerDTO1).isNotEqualTo(aIAnalyzerDTO2);
    }
}

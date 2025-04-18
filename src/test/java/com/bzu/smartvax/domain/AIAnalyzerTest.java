package com.bzu.smartvax.domain;

import static com.bzu.smartvax.domain.AIAnalyzerTestSamples.*;
import static com.bzu.smartvax.domain.FeedbackTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bzu.smartvax.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AIAnalyzerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AIAnalyzer.class);
        AIAnalyzer aIAnalyzer1 = getAIAnalyzerSample1();
        AIAnalyzer aIAnalyzer2 = new AIAnalyzer();
        assertThat(aIAnalyzer1).isNotEqualTo(aIAnalyzer2);

        aIAnalyzer2.setId(aIAnalyzer1.getId());
        assertThat(aIAnalyzer1).isEqualTo(aIAnalyzer2);

        aIAnalyzer2 = getAIAnalyzerSample2();
        assertThat(aIAnalyzer1).isNotEqualTo(aIAnalyzer2);
    }

    @Test
    void feedbackTest() {
        AIAnalyzer aIAnalyzer = getAIAnalyzerRandomSampleGenerator();
        Feedback feedbackBack = getFeedbackRandomSampleGenerator();

        aIAnalyzer.setFeedback(feedbackBack);
        assertThat(aIAnalyzer.getFeedback()).isEqualTo(feedbackBack);

        aIAnalyzer.feedback(null);
        assertThat(aIAnalyzer.getFeedback()).isNull();
    }
}

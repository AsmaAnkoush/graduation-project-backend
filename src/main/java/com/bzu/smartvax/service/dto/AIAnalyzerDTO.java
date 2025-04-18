package com.bzu.smartvax.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bzu.smartvax.domain.AIAnalyzer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AIAnalyzerDTO implements Serializable {

    private Long id;

    private String analysisResult;

    private FeedbackDTO feedback;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(String analysisResult) {
        this.analysisResult = analysisResult;
    }

    public FeedbackDTO getFeedback() {
        return feedback;
    }

    public void setFeedback(FeedbackDTO feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AIAnalyzerDTO)) {
            return false;
        }

        AIAnalyzerDTO aIAnalyzerDTO = (AIAnalyzerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aIAnalyzerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AIAnalyzerDTO{" +
            "id=" + getId() +
            ", analysisResult='" + getAnalysisResult() + "'" +
            ", feedback=" + getFeedback() +
            "}";
    }
}

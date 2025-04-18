package com.bzu.smartvax.service.dto;

public class FeedbackAnalysisResponseDTO {

    private String diagnosis;
    private String suggestions;

    public FeedbackAnalysisResponseDTO() {}

    public FeedbackAnalysisResponseDTO(String diagnosis, String suggestions) {
        this.diagnosis = diagnosis;
        this.suggestions = suggestions;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }
}


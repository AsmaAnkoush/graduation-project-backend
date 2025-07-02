package com.bzu.smartvax.service.dto;

public class FeedbackAnalysisResponseDTO {

    private String diagnosis;
    private String suggestions;
    private Long feedbackId; // تم إضافة هذا الحقل الجديد

    public FeedbackAnalysisResponseDTO() {}

    // تم تحديث الـ constructor ليقبل feedbackId
    public FeedbackAnalysisResponseDTO(String diagnosis, String suggestions, Long feedbackId) {
        this.diagnosis = diagnosis;
        this.suggestions = suggestions;
        this.feedbackId = feedbackId;
    }

    // Constructor الأصلي يمكن أن يبقى إذا كنت لا تزال تستخدمه في سياقات أخرى لا تتطلب الـ ID
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

    // Getters and Setters للحقل الجديد feedbackId
    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }
}

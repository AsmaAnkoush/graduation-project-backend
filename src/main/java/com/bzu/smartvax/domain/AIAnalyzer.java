package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A AIAnalyzer.
 */
@Entity
@Table(name = "ai_analyzer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AIAnalyzer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "analysis_result")
    private String analysisResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "vaccination" }, allowSetters = true)
    private Feedback feedback;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AIAnalyzer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnalysisResult() {
        return this.analysisResult;
    }

    public AIAnalyzer analysisResult(String analysisResult) {
        this.setAnalysisResult(analysisResult);
        return this;
    }

    public void setAnalysisResult(String analysisResult) {
        this.analysisResult = analysisResult;
    }

    public Feedback getFeedback() {
        return this.feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public AIAnalyzer feedback(Feedback feedback) {
        this.setFeedback(feedback);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AIAnalyzer)) {
            return false;
        }
        return getId() != null && getId().equals(((AIAnalyzer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AIAnalyzer{" +
            "id=" + getId() +
            ", analysisResult='" + getAnalysisResult() + "'" +
            "}";
    }
}

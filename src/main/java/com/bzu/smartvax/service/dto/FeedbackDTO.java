package com.bzu.smartvax.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bzu.smartvax.domain.Feedback} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackDTO implements Serializable {

    private Long id;

    private String messageText;

    private String sideEffects;

    private String treatment;

    private Instant dateSubmitted;

    private ParentDTO parent;

    private VaccinationDTO vaccination;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Instant getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Instant dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public ParentDTO getParent() {
        return parent;
    }

    public void setParent(ParentDTO parent) {
        this.parent = parent;
    }

    public VaccinationDTO getVaccination() {
        return vaccination;
    }

    public void setVaccination(VaccinationDTO vaccination) {
        this.vaccination = vaccination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackDTO)) {
            return false;
        }

        FeedbackDTO feedbackDTO = (FeedbackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feedbackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackDTO{" +
            "id=" + getId() +
            ", messageText='" + getMessageText() + "'" +
            ", sideEffects='" + getSideEffects() + "'" +
            ", treatment='" + getTreatment() + "'" +
            ", dateSubmitted='" + getDateSubmitted() + "'" +
            ", parent=" + getParent() +
            ", vaccination=" + getVaccination() +
            "}";
    }
}

package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Feedback.
 */
@Entity
@Table(name = "feedback")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Column(name = "side_effects")
    private String sideEffects;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "date_submitted")
    private Instant dateSubmitted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "children", "appointments", "reminders", "feedbacks" }, allowSetters = true)
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feedbacks", "scheduleVaccinations" }, allowSetters = true)
    private Vaccination vaccination;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Feedback id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageText() {
        return this.messageText;
    }

    public Feedback messageText(String messageText) {
        this.setMessageText(messageText);
        return this;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSideEffects() {
        return this.sideEffects;
    }

    public Feedback sideEffects(String sideEffects) {
        this.setSideEffects(sideEffects);
        return this;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getTreatment() {
        return this.treatment;
    }

    public Feedback treatment(String treatment) {
        this.setTreatment(treatment);
        return this;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Instant getDateSubmitted() {
        return this.dateSubmitted;
    }

    public Feedback dateSubmitted(Instant dateSubmitted) {
        this.setDateSubmitted(dateSubmitted);
        return this;
    }

    public void setDateSubmitted(Instant dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public Parent getParent() {
        return this.parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Feedback parent(Parent parent) {
        this.setParent(parent);
        return this;
    }

    public Vaccination getVaccination() {
        return this.vaccination;
    }

    public void setVaccination(Vaccination vaccination) {
        this.vaccination = vaccination;
    }

    public Feedback vaccination(Vaccination vaccination) {
        this.setVaccination(vaccination);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Feedback)) {
            return false;
        }
        return getId() != null && getId().equals(((Feedback) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Feedback{" +
            "id=" + getId() +
            ", messageText='" + getMessageText() + "'" +
            ", sideEffects='" + getSideEffects() + "'" +
            ", treatment='" + getTreatment() + "'" +
            ", dateSubmitted='" + getDateSubmitted() + "'" +
            "}";
    }
}

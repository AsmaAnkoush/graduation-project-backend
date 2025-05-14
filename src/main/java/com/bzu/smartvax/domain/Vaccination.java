package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Vaccination.
 */
@Entity
@Table(name = "vaccination")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vaccination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "side_effects")
    private String sideEffects;

    @Column(name = "target_age")
    private Integer targetAge;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "treatment")
    private String treatment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vaccination")
    @JsonIgnoreProperties(value = { "parent", "vaccination" }, allowSetters = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vaccination")
    @JsonIgnoreProperties(value = { "appointments", "child", "vaccination" }, allowSetters = true)
    private Set<ScheduleVaccination> scheduleVaccinations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vaccination id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Vaccination name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public Vaccination type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSideEffects() {
        return this.sideEffects;
    }

    public Vaccination sideEffects(String sideEffects) {
        this.setSideEffects(sideEffects);
        return this;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public Integer getTargetAge() {
        return this.targetAge;
    }

    public Vaccination targetAge(Integer targetAge) {
        this.setTargetAge(targetAge);
        return this;
    }

    public void setTargetAge(Integer targetAge) {
        this.targetAge = targetAge;
    }

    public String getStatus() {
        return this.status;
    }

    public Vaccination status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTreatment() {
        return this.treatment;
    }

    public Vaccination treatment(String treatment) {
        this.setTreatment(treatment);
        return this;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Set<Feedback> getFeedbacks() {
        return this.feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        if (this.feedbacks != null) {
            this.feedbacks.forEach(i -> i.setVaccination(null));
        }
        if (feedbacks != null) {
            feedbacks.forEach(i -> i.setVaccination(this));
        }
        this.feedbacks = feedbacks;
    }

    public Vaccination feedbacks(Set<Feedback> feedbacks) {
        this.setFeedbacks(feedbacks);
        return this;
    }

    public Vaccination addFeedbacks(Feedback feedback) {
        this.feedbacks.add(feedback);
        feedback.setVaccination(this);
        return this;
    }

    public Vaccination removeFeedbacks(Feedback feedback) {
        this.feedbacks.remove(feedback);
        feedback.setVaccination(null);
        return this;
    }

    public Set<ScheduleVaccination> getScheduleVaccinations() {
        return this.scheduleVaccinations;
    }

    public void setScheduleVaccinations(Set<ScheduleVaccination> scheduleVaccinations) {
        if (this.scheduleVaccinations != null) {
            this.scheduleVaccinations.forEach(i -> i.setVaccination(null));
        }
        if (scheduleVaccinations != null) {
            scheduleVaccinations.forEach(i -> i.setVaccination(this));
        }
        this.scheduleVaccinations = scheduleVaccinations;
    }

    public Vaccination scheduleVaccinations(Set<ScheduleVaccination> scheduleVaccinations) {
        this.setScheduleVaccinations(scheduleVaccinations);
        return this;
    }

    public Vaccination addScheduleVaccinations(ScheduleVaccination scheduleVaccination) {
        this.scheduleVaccinations.add(scheduleVaccination);
        scheduleVaccination.setVaccination(this);
        return this;
    }

    public Vaccination removeScheduleVaccinations(ScheduleVaccination scheduleVaccination) {
        this.scheduleVaccinations.remove(scheduleVaccination);
        scheduleVaccination.setVaccination(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vaccination)) {
            return false;
        }
        return getId() != null && getId().equals(((Vaccination) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vaccination{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", sideEffects='" + getSideEffects() + "'" +
            ", targetAge=" + getTargetAge() +
            ", status='" + getStatus() + "'" +
            ", treatment='" + getTreatment() + "'" +
            "}";
    }
}

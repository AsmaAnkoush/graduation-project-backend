package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
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

    @Column(name = "dose")
    private String dose;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "route_of_administration")
    private String routeOfAdministration;

    @Column(name = "side_effects")
    private String sideEffects;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vaccine_type_id", nullable = false)
    private VaccineType vaccineType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private VaccinationGroup group;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vaccination")
    @JsonIgnoreProperties(value = { "parent", "vaccination" }, allowSetters = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vaccination")
    @JsonIgnoreProperties(value = { "appointments", "child", "vaccination" }, allowSetters = true)
    private Set<ScheduleVaccination> scheduleVaccinations = new HashSet<>();

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public Vaccination sideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getRouteOfAdministration() {
        return routeOfAdministration;
    }

    public void setRouteOfAdministration(String routeOfAdministration) {
        this.routeOfAdministration = routeOfAdministration;
    }

    public VaccineType getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(VaccineType vaccineType) {
        this.vaccineType = vaccineType;
    }

    public VaccinationGroup getGroup() {
        return group;
    }

    public void setGroup(VaccinationGroup group) {
        this.group = group;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
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

    public Set<ScheduleVaccination> getScheduleVaccinations() {
        return scheduleVaccinations;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vaccination)) return false;
        return id != null && id.equals(((Vaccination) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Vaccination{" +
            "id=" +
            id +
            ", name='" +
            name +
            '\'' +
            ", dose='" +
            dose +
            '\'' +
            ", treatment='" +
            treatment +
            '\'' +
            ", routeOfAdministration='" +
            routeOfAdministration +
            '\'' +
            '}'
        );
    }

    public Vaccination id(Long id) {
        this.id = id;
        return this;
    }

    public Vaccination name(String name) {
        this.name = name;
        return this;
    }

    public Vaccination dose(String dose) {
        this.dose = dose;
        return this;
    }

    public Vaccination treatment(String treatment) {
        this.treatment = treatment;
        return this;
    }

    public Vaccination routeOfAdministration(String routeOfAdministration) {
        this.routeOfAdministration = routeOfAdministration;
        return this;
    }

    public Vaccination vaccineType(VaccineType vaccineType) {
        this.vaccineType = vaccineType;
        return this;
    }

    public Vaccination group(VaccinationGroup group) {
        this.group = group;
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

    public Vaccination feedbacks(Set<Feedback> feedbacks) {
        this.setFeedbacks(feedbacks);
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

    public Vaccination scheduleVaccinations(Set<ScheduleVaccination> scheduleVaccinations) {
        this.setScheduleVaccinations(scheduleVaccinations);
        return this;
    }
}

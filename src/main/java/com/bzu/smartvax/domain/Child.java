package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Child.
 */
@Entity
@Table(name = "child")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Child implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "weight", precision = 21, scale = 2)
    private BigDecimal weight;

    @Column(name = "height", precision = 21, scale = 2)
    private BigDecimal height;

    @JsonIgnoreProperties(value = { "child" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private HealthRecord healthRecord;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "child")
    @JsonIgnoreProperties(value = { "parent", "child", "schedule", "healthWorker" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "child")
    @JsonIgnoreProperties(value = { "appointments", "child", "vaccination" }, allowSetters = true)
    private Set<ScheduleVaccination> scheduleVaccinations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "children", "appointments", "reminders", "feedbacks" }, allowSetters = true)
    private Parent parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Child id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Child name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public Child dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public BigDecimal getWeight() {
        return this.weight;
    }

    public Child weight(BigDecimal weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getHeight() {
        return this.height;
    }

    public Child height(BigDecimal height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public HealthRecord getHealthRecord() {
        return this.healthRecord;
    }

    public void setHealthRecord(HealthRecord healthRecord) {
        this.healthRecord = healthRecord;
    }

    public Child healthRecord(HealthRecord healthRecord) {
        this.setHealthRecord(healthRecord);
        return this;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setChild(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setChild(this));
        }
        this.appointments = appointments;
    }

    public Child appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public Child addAppointments(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setChild(this);
        return this;
    }

    public Child removeAppointments(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setChild(null);
        return this;
    }

    public Set<ScheduleVaccination> getScheduleVaccinations() {
        return this.scheduleVaccinations;
    }

    public void setScheduleVaccinations(Set<ScheduleVaccination> scheduleVaccinations) {
        if (this.scheduleVaccinations != null) {
            this.scheduleVaccinations.forEach(i -> i.setChild(null));
        }
        if (scheduleVaccinations != null) {
            scheduleVaccinations.forEach(i -> i.setChild(this));
        }
        this.scheduleVaccinations = scheduleVaccinations;
    }

    public Child scheduleVaccinations(Set<ScheduleVaccination> scheduleVaccinations) {
        this.setScheduleVaccinations(scheduleVaccinations);
        return this;
    }

    public Child addScheduleVaccinations(ScheduleVaccination scheduleVaccination) {
        this.scheduleVaccinations.add(scheduleVaccination);
        scheduleVaccination.setChild(this);
        return this;
    }

    public Child removeScheduleVaccinations(ScheduleVaccination scheduleVaccination) {
        this.scheduleVaccinations.remove(scheduleVaccination);
        scheduleVaccination.setChild(null);
        return this;
    }

    public Parent getParent() {
        return this.parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Child parent(Parent parent) {
        this.setParent(parent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Child)) {
            return false;
        }
        return getId() != null && getId().equals(((Child) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Child{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dob='" + getDob() + "'" +
            ", weight=" + getWeight() +
            ", height=" + getHeight() +
            "}";
    }
}

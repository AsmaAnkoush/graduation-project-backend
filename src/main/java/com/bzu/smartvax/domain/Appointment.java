package com.bzu.smartvax.domain;

import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Appointment.
 */
@Entity
@Table(name = "appointment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "appointment_date", nullable = false)
    private Instant appointmentDate;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "children", "appointments", "reminders", "feedbacks" }, allowSetters = true)
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "healthRecord", "appointments", "scheduleVaccinations", "parent" }, allowSetters = true)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "appointments", "child", "vaccination" }, allowSetters = true)
    private ScheduleVaccination schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "appointments" }, allowSetters = true)
    private HealthWorker healthWorker;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Appointment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAppointmentDate() {
        return this.appointmentDate;
    }

    public Appointment appointmentDate(Instant appointmentDate) {
        this.setAppointmentDate(appointmentDate);
        return this;
    }

    public void setAppointmentDate(Instant appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return this.status;
    }

    public Appointment status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Parent getParent() {
        return this.parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Appointment parent(Parent parent) {
        this.setParent(parent);
        return this;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Appointment child(Child child) {
        this.setChild(child);
        return this;
    }

    public ScheduleVaccination getSchedule() {
        return this.schedule;
    }

    public void setSchedule(ScheduleVaccination scheduleVaccination) {
        this.schedule = scheduleVaccination;
    }

    public Appointment schedule(ScheduleVaccination scheduleVaccination) {
        this.setSchedule(scheduleVaccination);
        return this;
    }

    public HealthWorker getHealthWorker() {
        return this.healthWorker;
    }

    public void setHealthWorker(HealthWorker healthWorker) {
        this.healthWorker = healthWorker;
    }

    public Appointment healthWorker(HealthWorker healthWorker) {
        this.setHealthWorker(healthWorker);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appointment)) {
            return false;
        }
        return getId() != null && getId().equals(((Appointment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appointment{" +
            "id=" + getId() +
            ", appointmentDate='" + getAppointmentDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}

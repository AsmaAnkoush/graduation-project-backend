package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A ScheduleVaccination.
 */
@Entity
@Table(name = "schedule_vaccination")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduleVaccination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule")
    @JsonIgnoreProperties(value = { "parent", "child", "schedule", "healthWorker" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "healthRecord", "appointments", "scheduleVaccinations", "parent" }, allowSetters = true)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feedbacks", "scheduleVaccinations" }, allowSetters = true)
    private Vaccination vaccination;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ScheduleVaccination id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getScheduledDate() {
        return this.scheduledDate;
    }

    public ScheduleVaccination scheduledDate(LocalDate scheduledDate) {
        this.setScheduledDate(scheduledDate);
        return this;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getStatus() {
        return this.status;
    }

    public ScheduleVaccination status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setSchedule(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setSchedule(this));
        }
        this.appointments = appointments;
    }

    public ScheduleVaccination appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public ScheduleVaccination addAppointments(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setSchedule(this);
        return this;
    }

    public ScheduleVaccination removeAppointments(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setSchedule(null);
        return this;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public ScheduleVaccination child(Child child) {
        this.setChild(child);
        return this;
    }

    public Vaccination getVaccination() {
        return this.vaccination;
    }

    public void setVaccination(Vaccination vaccination) {
        this.vaccination = vaccination;
    }

    public ScheduleVaccination vaccination(Vaccination vaccination) {
        this.setVaccination(vaccination);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduleVaccination)) {
            return false;
        }
        return getId() != null && getId().equals(((ScheduleVaccination) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleVaccination{" +
            "id=" + getId() +
            ", scheduledDate='" + getScheduledDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}

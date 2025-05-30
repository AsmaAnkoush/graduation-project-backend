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

    @ManyToMany(mappedBy = "schedules")
    @JsonIgnoreProperties(
        value = { "parent", "child", "schedules", "healthWorker", "vaccinationCenter", "requestedNewCenter" },
        allowSetters = true
    )
    private Set<Appointment> appointments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "healthRecord", "appointments", "scheduleVaccinations", "parent" }, allowSetters = true)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feedbacks", "scheduleVaccinations" }, allowSetters = true)
    private Vaccination vaccination;

    @ManyToOne
    @JoinColumn(name = "vaccination_group_id")
    private VaccinationGroup vaccinationGroup;

    // === Getters & Setters ===

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
        this.appointments = appointments;
    }

    public ScheduleVaccination appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public ScheduleVaccination addAppointments(Appointment appointment) {
        this.appointments.add(appointment);
        return this;
    }

    public ScheduleVaccination removeAppointments(Appointment appointment) {
        this.appointments.remove(appointment);
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

    public VaccinationGroup getVaccinationGroup() {
        return vaccinationGroup;
    }

    public void setVaccinationGroup(VaccinationGroup vaccinationGroup) {
        this.vaccinationGroup = vaccinationGroup;
    }

    // === equals, hashCode, toString ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleVaccination)) return false;
        return getId() != null && getId().equals(((ScheduleVaccination) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "ScheduleVaccination{" +
            "id=" +
            getId() +
            ", scheduledDate='" +
            getScheduledDate() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            "}"
        );
    }
}

package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

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

    @Column(name = "reschedule_reason")
    private String rescheduleReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "children", "appointments", "reminders", "feedbacks" }, allowSetters = true, allowGetters = true)
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "healthRecord", "appointments", "scheduleVaccinations", "parent" },
        allowSetters = true,
        allowGetters = true
    )
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccination_center_id")
    @JsonIgnoreProperties(value = { "healthWorkers", "children" }, allowSetters = true, allowGetters = true)
    private VaccinationCenter vaccinationCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_center_id")
    @JsonIgnoreProperties(value = { "healthWorkers", "children" }, allowSetters = true, allowGetters = true)
    private VaccinationCenter requestedNewCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "appointments" }, allowSetters = true, allowGetters = true)
    private HealthWorker healthWorker;

    @ManyToMany
    @JoinTable(
        name = "appointment_schedule_vaccination",
        joinColumns = @JoinColumn(name = "appointment_id"),
        inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    @JsonIgnoreProperties(value = { "appointments", "child", "vaccination" }, allowSetters = true, allowGetters = true)
    private List<ScheduleVaccination> schedules;

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public Appointment id(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAppointmentDate() {
        return appointmentDate;
    }

    public Appointment appointmentDate(Instant appointmentDate) {
        this.appointmentDate = appointmentDate;
        return this;
    }

    public void setAppointmentDate(Instant appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public Appointment status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRescheduleReason() {
        return rescheduleReason;
    }

    public Appointment rescheduleReason(String rescheduleReason) {
        this.rescheduleReason = rescheduleReason;
        return this;
    }

    public void setRescheduleReason(String rescheduleReason) {
        this.rescheduleReason = rescheduleReason;
    }

    public Parent getParent() {
        return parent;
    }

    public Appointment parent(Parent parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Child getChild() {
        return child;
    }

    public Appointment child(Child child) {
        this.child = child;
        return this;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public VaccinationCenter getVaccinationCenter() {
        return vaccinationCenter;
    }

    public Appointment vaccinationCenter(VaccinationCenter vaccinationCenter) {
        this.vaccinationCenter = vaccinationCenter;
        return this;
    }

    public void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
        this.vaccinationCenter = vaccinationCenter;
    }

    public VaccinationCenter getRequestedNewCenter() {
        return requestedNewCenter;
    }

    public Appointment requestedNewCenter(VaccinationCenter requestedNewCenter) {
        this.requestedNewCenter = requestedNewCenter;
        return this;
    }

    public void setRequestedNewCenter(VaccinationCenter requestedNewCenter) {
        this.requestedNewCenter = requestedNewCenter;
    }

    public HealthWorker getHealthWorker() {
        return healthWorker;
    }

    public Appointment healthWorker(HealthWorker healthWorker) {
        this.healthWorker = healthWorker;
        return this;
    }

    public void setHealthWorker(HealthWorker healthWorker) {
        this.healthWorker = healthWorker;
    }

    public List<ScheduleVaccination> getSchedules() {
        return schedules;
    }

    public Appointment schedules(List<ScheduleVaccination> schedules) {
        this.schedules = schedules;
        return this;
    }

    public void setSchedules(List<ScheduleVaccination> schedules) {
        this.schedules = schedules;
    }

    // === equals/hashCode/toString ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        return getId() != null && getId().equals(((Appointment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Appointment{" + "id=" + getId() + ", appointmentDate=" + getAppointmentDate() + ", status='" + getStatus() + '\'' + '}';
    }
}

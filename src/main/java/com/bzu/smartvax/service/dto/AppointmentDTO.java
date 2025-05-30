package com.bzu.smartvax.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.bzu.smartvax.domain.Appointment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppointmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant appointmentDate;

    @NotNull
    private String status;

    private String rescheduleReason;

    private ParentDTO parent;

    private ChildDTO child;

    private List<ScheduleVaccinationDTO> scheduleVaccinations; // ✅ الجديد

    private HealthWorkerDTO healthWorker;

    private VaccinationCenterDTO vaccinationCenter;

    private VaccinationCenterDTO requestedNewCenter;

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Instant appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRescheduleReason() {
        return rescheduleReason;
    }

    public void setRescheduleReason(String rescheduleReason) {
        this.rescheduleReason = rescheduleReason;
    }

    public ParentDTO getParent() {
        return parent;
    }

    public void setParent(ParentDTO parent) {
        this.parent = parent;
    }

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    public List<ScheduleVaccinationDTO> getScheduleVaccinations() {
        return scheduleVaccinations;
    }

    public void setScheduleVaccinations(List<ScheduleVaccinationDTO> scheduleVaccinations) {
        this.scheduleVaccinations = scheduleVaccinations;
    }

    public HealthWorkerDTO getHealthWorker() {
        return healthWorker;
    }

    public void setHealthWorker(HealthWorkerDTO healthWorker) {
        this.healthWorker = healthWorker;
    }

    public VaccinationCenterDTO getVaccinationCenter() {
        return vaccinationCenter;
    }

    public void setVaccinationCenter(VaccinationCenterDTO vaccinationCenter) {
        this.vaccinationCenter = vaccinationCenter;
    }

    public VaccinationCenterDTO getRequestedNewCenter() {
        return requestedNewCenter;
    }

    public void setRequestedNewCenter(VaccinationCenterDTO requestedNewCenter) {
        this.requestedNewCenter = requestedNewCenter;
    }

    // === equals/hashCode/toString ===

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentDTO that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        String vaccines = scheduleVaccinations != null
            ? scheduleVaccinations
                .stream()
                .map(sv -> sv.getVaccination() != null ? sv.getVaccination().getName() : "???")
                .collect(Collectors.joining(", "))
            : "No vaccines";

        return (
            "AppointmentDTO{" +
            "id=" +
            getId() +
            ", appointmentDate='" +
            getAppointmentDate() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", rescheduleReason='" +
            getRescheduleReason() +
            "'" +
            ", parent=" +
            getParent() +
            ", child=" +
            getChild() +
            ", scheduleVaccinations=" +
            vaccines +
            ", healthWorker=" +
            getHealthWorker() +
            ", vaccinationCenter=" +
            (vaccinationCenter != null ? vaccinationCenter.getId() : null) +
            ", requestedNewCenter=" +
            (requestedNewCenter != null ? requestedNewCenter.getId() : null) +
            '}'
        );
    }
}

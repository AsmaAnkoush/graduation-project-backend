package com.bzu.smartvax.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

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

    private ParentDTO parent;

    private ChildDTO child;

    private ScheduleVaccinationDTO schedule;

    private HealthWorkerDTO healthWorker;

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

    public ScheduleVaccinationDTO getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleVaccinationDTO schedule) {
        this.schedule = schedule;
    }

    public HealthWorkerDTO getHealthWorker() {
        return healthWorker;
    }

    public void setHealthWorker(HealthWorkerDTO healthWorker) {
        this.healthWorker = healthWorker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentDTO)) {
            return false;
        }

        AppointmentDTO appointmentDTO = (AppointmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentDTO{" +
            "id=" + getId() +
            ", appointmentDate='" + getAppointmentDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", parent=" + getParent() +
            ", child=" + getChild() +
            ", schedule=" + getSchedule() +
            ", healthWorker=" + getHealthWorker() +
            "}";
    }
}

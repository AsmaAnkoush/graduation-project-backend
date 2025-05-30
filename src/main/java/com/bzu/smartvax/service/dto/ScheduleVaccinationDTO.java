package com.bzu.smartvax.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.bzu.smartvax.domain.ScheduleVaccination} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduleVaccinationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate scheduledDate;

    @NotNull
    private String status;

    private ChildDTO child;

    private VaccinationDTO vaccination;

    private VaccinationGroupDTO vaccinationGroup; // ✅ الحقل الجديد

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    public VaccinationDTO getVaccination() {
        return vaccination;
    }

    public void setVaccination(VaccinationDTO vaccination) {
        this.vaccination = vaccination;
    }

    public VaccinationGroupDTO getVaccinationGroup() {
        return vaccinationGroup;
    }

    public void setVaccinationGroup(VaccinationGroupDTO vaccinationGroup) {
        this.vaccinationGroup = vaccinationGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduleVaccinationDTO scheduleVaccinationDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, scheduleVaccinationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduleVaccinationDTO{" +
            "id=" + getId() +
            ", scheduledDate='" + getScheduledDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", child=" + getChild() +
            ", vaccination=" + getVaccination() +
            ", vaccinationGroup=" + getVaccinationGroup() + // ✅ أضيفت
            "}";
    }
}

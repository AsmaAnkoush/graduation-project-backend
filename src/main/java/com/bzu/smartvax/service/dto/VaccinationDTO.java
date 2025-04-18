package com.bzu.smartvax.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.bzu.smartvax.domain.Vaccination} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VaccinationDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String type;

    private LocalDate dateGiven;

    private String sideEffects;

    private Integer targetAge;

    @NotNull
    private String status;

    private String treatment;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDateGiven() {
        return dateGiven;
    }

    public void setDateGiven(LocalDate dateGiven) {
        this.dateGiven = dateGiven;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public Integer getTargetAge() {
        return targetAge;
    }

    public void setTargetAge(Integer targetAge) {
        this.targetAge = targetAge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccinationDTO)) {
            return false;
        }

        VaccinationDTO vaccinationDTO = (VaccinationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vaccinationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccinationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", dateGiven='" + getDateGiven() + "'" +
            ", sideEffects='" + getSideEffects() + "'" +
            ", targetAge=" + getTargetAge() +
            ", status='" + getStatus() + "'" +
            ", treatment='" + getTreatment() + "'" +
            "}";
    }
}

package com.bzu.smartvax.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bzu.smartvax.domain.HealthRecord} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HealthRecordDTO implements Serializable {

    private Long id;

    private String sensitivity;

    private Boolean diabetes;

    private Boolean highBloodPressure;

    private String geneticDiseases;

    private String bloodType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(String sensitivity) {
        this.sensitivity = sensitivity;
    }

    public Boolean getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(Boolean diabetes) {
        this.diabetes = diabetes;
    }

    public Boolean getHighBloodPressure() {
        return highBloodPressure;
    }

    public void setHighBloodPressure(Boolean highBloodPressure) {
        this.highBloodPressure = highBloodPressure;
    }

    public String getGeneticDiseases() {
        return geneticDiseases;
    }

    public void setGeneticDiseases(String geneticDiseases) {
        this.geneticDiseases = geneticDiseases;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HealthRecordDTO)) {
            return false;
        }

        HealthRecordDTO healthRecordDTO = (HealthRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, healthRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HealthRecordDTO{" +
            "id=" + getId() +
            ", sensitivity='" + getSensitivity() + "'" +
            ", diabetes='" + getDiabetes() + "'" +
            ", highBloodPressure='" + getHighBloodPressure() + "'" +
            ", geneticDiseases='" + getGeneticDiseases() + "'" +
            ", bloodType='" + getBloodType() + "'" +
            "}";
    }
}

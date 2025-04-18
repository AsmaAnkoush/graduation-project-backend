package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A HealthRecord.
 */
@Entity
@Table(name = "health_record")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HealthRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sensitivity")
    private String sensitivity;

    @Column(name = "diabetes")
    private Boolean diabetes;

    @Column(name = "high_blood_pressure")
    private Boolean highBloodPressure;

    @Column(name = "genetic_diseases")
    private String geneticDiseases;

    @Column(name = "blood_type")
    private String bloodType;

    @JsonIgnoreProperties(value = { "healthRecord", "appointments", "scheduleVaccinations", "parent" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "healthRecord")
    private Child child;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HealthRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensitivity() {
        return this.sensitivity;
    }

    public HealthRecord sensitivity(String sensitivity) {
        this.setSensitivity(sensitivity);
        return this;
    }

    public void setSensitivity(String sensitivity) {
        this.sensitivity = sensitivity;
    }

    public Boolean getDiabetes() {
        return this.diabetes;
    }

    public HealthRecord diabetes(Boolean diabetes) {
        this.setDiabetes(diabetes);
        return this;
    }

    public void setDiabetes(Boolean diabetes) {
        this.diabetes = diabetes;
    }

    public Boolean getHighBloodPressure() {
        return this.highBloodPressure;
    }

    public HealthRecord highBloodPressure(Boolean highBloodPressure) {
        this.setHighBloodPressure(highBloodPressure);
        return this;
    }

    public void setHighBloodPressure(Boolean highBloodPressure) {
        this.highBloodPressure = highBloodPressure;
    }

    public String getGeneticDiseases() {
        return this.geneticDiseases;
    }

    public HealthRecord geneticDiseases(String geneticDiseases) {
        this.setGeneticDiseases(geneticDiseases);
        return this;
    }

    public void setGeneticDiseases(String geneticDiseases) {
        this.geneticDiseases = geneticDiseases;
    }

    public String getBloodType() {
        return this.bloodType;
    }

    public HealthRecord bloodType(String bloodType) {
        this.setBloodType(bloodType);
        return this;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        if (this.child != null) {
            this.child.setHealthRecord(null);
        }
        if (child != null) {
            child.setHealthRecord(this);
        }
        this.child = child;
    }

    public HealthRecord child(Child child) {
        this.setChild(child);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HealthRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((HealthRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HealthRecord{" +
            "id=" + getId() +
            ", sensitivity='" + getSensitivity() + "'" +
            ", diabetes='" + getDiabetes() + "'" +
            ", highBloodPressure='" + getHighBloodPressure() + "'" +
            ", geneticDiseases='" + getGeneticDiseases() + "'" +
            ", bloodType='" + getBloodType() + "'" +
            "}";
    }
}

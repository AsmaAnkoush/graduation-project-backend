package com.bzu.smartvax.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "child")
public class Child implements Serializable {

    @Id
    @Column(length = 50)
    private String id;

    private String name;

    private LocalDate dob;

    private BigDecimal weight;

    private BigDecimal height;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "health_record_id")
    private HealthRecord healthRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccination_center_id")
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.SET_NULL)
    private VaccinationCenter vaccinationCenter;

    // === Getters and Setters ===

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public HealthRecord getHealthRecord() {
        return healthRecord;
    }

    public void setHealthRecord(HealthRecord healthRecord) {
        this.healthRecord = healthRecord;
    }

    public VaccinationCenter getVaccinationCenter() {
        return vaccinationCenter;
    }

    public void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
        this.vaccinationCenter = vaccinationCenter;
    }

    // === Builder-style methods ===

    public Child id(String id) {
        this.setId(id);
        return this;
    }

    public Child name(String name) {
        this.setName(name);
        return this;
    }

    public Child dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public Child parent(Parent parent) {
        this.setParent(parent);
        return this;
    }

    public Child healthRecord(HealthRecord healthRecord) {
        this.setHealthRecord(healthRecord);
        return this;
    }

    public Child vaccinationCenter(VaccinationCenter vaccinationCenter) {
        this.setVaccinationCenter(vaccinationCenter);
        return this;
    }

    public void setVaccinationCenterId(Long centerId) {
        if (centerId == null) {
            this.vaccinationCenter = null;
            return;
        }
        if (this.vaccinationCenter == null) {
            this.vaccinationCenter = new VaccinationCenter();
        }
        this.vaccinationCenter.setId(centerId);
    }
}

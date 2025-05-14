// ✅ Child.java - محدث بالكامل
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

    // Getters and Setters
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

    // ✅ Builder-style methods
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
}

package com.bzu.smartvax.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "additional_vaccines")
public class AdditionalVaccine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "min_age_months")
    private Integer minAgeMonths;

    @Column(name = "dose_count")
    private Integer doseCount;

    @Column(name = "notes")
    private String notes;

    // ==== Getters and Setters ====

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

    public Integer getMinAgeMonths() {
        return minAgeMonths;
    }

    public void setMinAgeMonths(Integer minAgeMonths) {
        this.minAgeMonths = minAgeMonths;
    }

    public Integer getDoseCount() {
        return doseCount;
    }

    public void setDoseCount(Integer doseCount) {
        this.doseCount = doseCount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

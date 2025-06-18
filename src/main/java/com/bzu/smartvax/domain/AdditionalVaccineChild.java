package com.bzu.smartvax.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "additional_vaccine_child")
public class AdditionalVaccineChild implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "additional_vaccine_id", nullable = false)
    private AdditionalVaccine additionalVaccine;

    @Column(name = "status")
    private String status; // PENDING or COMPLETED

    @Column(name = "date_of_administration")
    private LocalDate dateOfAdministration;

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public AdditionalVaccine getAdditionalVaccine() {
        return additionalVaccine;
    }

    public void setAdditionalVaccine(AdditionalVaccine additionalVaccine) {
        this.additionalVaccine = additionalVaccine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateOfAdministration() {
        return dateOfAdministration;
    }

    public void setDateOfAdministration(LocalDate dateOfAdministration) {
        this.dateOfAdministration = dateOfAdministration;
    }
}

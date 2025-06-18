package com.bzu.smartvax.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class AdditionalVaccineChildDTO implements Serializable {

    private Long id;
    private String status;
    private LocalDate dateOfAdministration;

    private ChildDTO child;
    private AdditionalVaccineDTO additionalVaccine;

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    public AdditionalVaccineDTO getAdditionalVaccine() {
        return additionalVaccine;
    }

    public void setAdditionalVaccine(AdditionalVaccineDTO additionalVaccine) {
        this.additionalVaccine = additionalVaccine;
    }
}

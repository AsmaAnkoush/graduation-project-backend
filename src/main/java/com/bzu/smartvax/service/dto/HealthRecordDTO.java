package com.bzu.smartvax.service.dto;

public class HealthRecordDTO {

    private Long id;
    private String sensitivity;
    private Boolean diabetes;
    private Boolean highBloodPressure;
    private String geneticDiseases;
    private String bloodType;

    // Getters and Setters
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
}

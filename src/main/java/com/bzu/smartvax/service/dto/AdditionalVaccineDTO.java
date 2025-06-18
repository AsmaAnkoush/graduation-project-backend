package com.bzu.smartvax.service.dto;

public class AdditionalVaccineDTO {

    private Long id;
    private String name;
    private Integer minAgeMonths;
    private Integer doseCount;
    private String notes;

    // Constructors
    public AdditionalVaccineDTO() {}

    public AdditionalVaccineDTO(Long id, String name, Integer minAgeMonths, Integer doseCount, String notes) {
        this.id = id;
        this.name = name;
        this.minAgeMonths = minAgeMonths;
        this.doseCount = doseCount;
        this.notes = notes;
    }

    // Getters and Setters
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

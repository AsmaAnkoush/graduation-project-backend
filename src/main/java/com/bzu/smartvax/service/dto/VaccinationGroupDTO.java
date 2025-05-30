package com.bzu.smartvax.service.dto;

public class VaccinationGroupDTO {

    private Long id;
    private String name;
    private Integer targetAgeDays;
    private Integer shiftThresholdDays;

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

    public Integer getTargetAgeDays() {
        return targetAgeDays;
    }

    public void setTargetAgeDays(Integer targetAgeDays) {
        this.targetAgeDays = targetAgeDays;
    }

    public Integer getShiftThresholdDays() {
        return shiftThresholdDays;
    }

    public void setShiftThresholdDays(Integer shiftThresholdDays) {
        this.shiftThresholdDays = shiftThresholdDays;
    }
}

package com.bzu.smartvax.service.dto;

import java.time.LocalDateTime;

public class ChildGrowthRecordDTO {

    private String childId;
    private Double weight;
    private Double height;

    private Double minWeight;
    private Double maxWeight;
    private Double minHeight;
    private Double maxHeight;

    private Integer ageInDays;
    private LocalDateTime dateRecorded;

    public ChildGrowthRecordDTO() {}

    public ChildGrowthRecordDTO(Double minWeight, Double maxWeight, Double minHeight, Double maxHeight) {
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Double minWeight) {
        this.minWeight = minWeight;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Double getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Double minHeight) {
        this.minHeight = minHeight;
    }

    public Double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Integer getAgeInDays() {
        return ageInDays;
    }

    public void setAgeInDays(Integer ageInDays) {
        this.ageInDays = ageInDays;
    }

    public LocalDateTime getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(LocalDateTime dateRecorded) {
        this.dateRecorded = dateRecorded;
    }
}

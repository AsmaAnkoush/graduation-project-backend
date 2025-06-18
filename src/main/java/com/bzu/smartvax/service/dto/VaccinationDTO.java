package com.bzu.smartvax.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bzu.smartvax.domain.Vaccination} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VaccinationDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String dose;

    private String treatment;

    private String routeOfAdministration;

    // ✅ معلومات نوع اللقاح
    private Long vaccineTypeId;
    private String vaccineTypeName;

    // ✅ معلومات مجموعة التطعيم
    private Long groupId;
    private String groupName;
    private Integer targetAgeDays;

    private String sideEffects;

    private VaccineTypeDTO vaccineType;
    private VaccinationGroupDTO group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccinationGroupDTO getGroup() {
        return group;
    }

    public void setGroup(VaccinationGroupDTO group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getRouteOfAdministration() {
        return routeOfAdministration;
    }

    public void setRouteOfAdministration(String routeOfAdministration) {
        this.routeOfAdministration = routeOfAdministration;
    }

    public Long getVaccineTypeId() {
        return vaccineTypeId;
    }

    public void setVaccineTypeId(Long vaccineTypeId) {
        this.vaccineTypeId = vaccineTypeId;
    }

    public String getVaccineTypeName() {
        return vaccineTypeName;
    }

    public void setVaccineTypeName(String vaccineTypeName) {
        this.vaccineTypeName = vaccineTypeName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getTargetAgeDays() {
        return targetAgeDays;
    }

    public void setTargetAgeDays(Integer targetAgeDays) {
        this.targetAgeDays = targetAgeDays;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public VaccineTypeDTO getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(VaccineTypeDTO vaccineType) {
        this.vaccineType = vaccineType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VaccinationDTO)) return false;
        VaccinationDTO that = (VaccinationDTO) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "VaccinationDTO{" +
            "id=" +
            id +
            ", name='" +
            name +
            '\'' +
            ", dose='" +
            dose +
            '\'' +
            ", treatment='" +
            treatment +
            '\'' +
            ", routeOfAdministration='" +
            routeOfAdministration +
            '\'' +
            ", vaccineTypeId=" +
            vaccineTypeId +
            ", vaccineTypeName='" +
            vaccineTypeName +
            '\'' +
            ", groupId=" +
            groupId +
            ", groupName='" +
            groupName +
            '\'' +
            ", targetAgeDays=" +
            targetAgeDays +
            ", sideEffects='" +
            sideEffects +
            '\'' +
            '}'
        );
    }
}

package com.bzu.smartvax.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "vaccination_group")
public class VaccinationGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "target_age_days", nullable = false)
    private Integer targetAgeDays;

    @Column(name = "shift_threshold_days", nullable = false)
    private Integer shiftThresholdDays;

    // العلاقة العكسية: مجموعة تحتوي تطعيمات مجدولة
    @OneToMany(mappedBy = "vaccinationGroup")
    private List<ScheduleVaccination> scheduleVaccinations;

    // === Getters & Setters ===

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

    public List<ScheduleVaccination> getScheduleVaccinations() {
        return scheduleVaccinations;
    }

    public void setScheduleVaccinations(List<ScheduleVaccination> scheduleVaccinations) {
        this.scheduleVaccinations = scheduleVaccinations;
    }
}

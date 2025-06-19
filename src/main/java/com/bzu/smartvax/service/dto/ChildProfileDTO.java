package com.bzu.smartvax.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ChildProfileDTO {

    public String id;
    public String name;
    public LocalDate dob;
    public String gender;
    public BigDecimal weight;
    public BigDecimal height;
    public String address;
    public String parentName;
    public String phone;
    private List<ScheduleVaccinationDTO> vaccinations;

    // ✅ Getter & Setter for vaccinations

    public List<ScheduleVaccinationDTO> getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(List<ScheduleVaccinationDTO> vaccinations) {
        this.vaccinations = vaccinations;
    }
}

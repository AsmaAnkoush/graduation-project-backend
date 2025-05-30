package com.bzu.smartvax.service.dto;

import java.io.Serializable;

public class VaccinationCenterDTO implements Serializable {

    private Long id;
    private String name;
    private String location;

    // Getters & Setters

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

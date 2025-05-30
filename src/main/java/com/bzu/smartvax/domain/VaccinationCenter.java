package com.bzu.smartvax.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "vaccination_center")
public class VaccinationCenter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

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

    public VaccinationCenter setName(String name) {
        this.name = name;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public VaccinationCenter setLocation(String location) {
        this.location = location;
        return this;
    }
}

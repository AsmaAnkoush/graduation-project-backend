package com.bzu.smartvax.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "additional_vaccines")
public class AdditionalVaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Constructors
    public AdditionalVaccine() {}

    public AdditionalVaccine(String name) {
        this.name = name;
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
}

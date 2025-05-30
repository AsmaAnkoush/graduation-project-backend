package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A HealthWorker.
 */
@Entity
@Table(name = "health_worker")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HealthWorker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "age")
    private Integer age;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "location")
    private String location;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "healthWorker")
    @JsonIgnoreProperties(value = { "parent", "child", "schedule", "healthWorker" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccination_center_id")
    @JsonIgnoreProperties(value = { "healthWorkers" }, allowSetters = true)
    private VaccinationCenter vaccinationCenter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HealthWorker id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public HealthWorker username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public HealthWorker password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return this.phone;
    }

    public HealthWorker phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return this.age;
    }

    public HealthWorker age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public HealthWorker name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return this.gender;
    }

    public HealthWorker gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return this.location;
    }

    public HealthWorker location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return this.email;
    }

    public HealthWorker email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return this.role;
    }

    public HealthWorker role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setHealthWorker(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setHealthWorker(this));
        }
        this.appointments = appointments;
    }

    public HealthWorker appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public HealthWorker addAppointments(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setHealthWorker(this);
        return this;
    }

    public HealthWorker removeAppointments(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setHealthWorker(null);
        return this;
    }

    public VaccinationCenter getVaccinationCenter() {
        return this.vaccinationCenter;
    }

    public void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
        this.vaccinationCenter = vaccinationCenter;
    }

    public HealthWorker vaccinationCenter(VaccinationCenter vaccinationCenter) {
        this.setVaccinationCenter(vaccinationCenter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HealthWorker)) return false;
        return getId() != null && getId().equals(((HealthWorker) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "HealthWorker{" +
            "id=" +
            getId() +
            ", username='" +
            getUsername() +
            "'" +
            ", password='" +
            getPassword() +
            "'" +
            ", phone='" +
            getPhone() +
            "'" +
            ", age=" +
            getAge() +
            ", name='" +
            getName() +
            "'" +
            ", gender='" +
            getGender() +
            "'" +
            ", location='" +
            getLocation() +
            "'" +
            ", email='" +
            getEmail() +
            "'" +
            ", role='" +
            getRole() +
            "'" +
            "}"
        );
    }
}

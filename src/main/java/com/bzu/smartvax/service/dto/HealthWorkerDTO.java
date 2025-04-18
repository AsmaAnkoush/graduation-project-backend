package com.bzu.smartvax.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bzu.smartvax.domain.HealthWorker} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HealthWorkerDTO implements Serializable {

    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    private Integer age;

    @NotNull
    private String name;

    private String gender;

    private String location;

    @NotNull
    private String email;

    @NotNull
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HealthWorkerDTO)) {
            return false;
        }

        HealthWorkerDTO healthWorkerDTO = (HealthWorkerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, healthWorkerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HealthWorkerDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", phone='" + getPhone() + "'" +
            ", age=" + getAge() +
            ", name='" + getName() + "'" +
            ", gender='" + getGender() + "'" +
            ", location='" + getLocation() + "'" +
            ", email='" + getEmail() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}

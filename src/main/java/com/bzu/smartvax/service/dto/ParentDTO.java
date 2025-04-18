package com.bzu.smartvax.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.bzu.smartvax.domain.Parent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParentDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String phone;

    private LocalDate dob;

    @NotNull
    private String role;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
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
        if (!(o instanceof ParentDTO)) {
            return false;
        }

        ParentDTO parentDTO = (ParentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", dob='" + getDob() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}

package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Parent.
 */
@Entity
@Table(name = "parent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "dob")
    private LocalDate dob;

    @NotNull
    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @JsonIgnoreProperties(value = { "healthRecord", "appointments", "scheduleVaccinations", "parent" }, allowSetters = true)
    private Set<Child> children = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @JsonIgnoreProperties(value = { "parent", "child", "schedule", "healthWorker" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipient")
    @JsonIgnoreProperties(value = { "appointment", "recipient" }, allowSetters = true)
    private Set<Reminder> reminders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @JsonIgnoreProperties(value = { "parent", "vaccination" }, allowSetters = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Parent name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Parent phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public Parent dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getRole() {
        return this.role;
    }

    public Parent role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Child> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Child> children) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (children != null) {
            children.forEach(i -> i.setParent(this));
        }
        this.children = children;
    }

    public Parent children(Set<Child> children) {
        this.setChildren(children);
        return this;
    }

    public Parent addChildren(Child child) {
        this.children.add(child);
        child.setParent(this);
        return this;
    }

    public Parent removeChildren(Child child) {
        this.children.remove(child);
        child.setParent(null);
        return this;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setParent(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setParent(this));
        }
        this.appointments = appointments;
    }

    public Parent appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public Parent addAppointments(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setParent(this);
        return this;
    }

    public Parent removeAppointments(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setParent(null);
        return this;
    }

    public Set<Reminder> getReminders() {
        return this.reminders;
    }

    public void setReminders(Set<Reminder> reminders) {
        if (this.reminders != null) {
            this.reminders.forEach(i -> i.setRecipient(null));
        }
        if (reminders != null) {
            reminders.forEach(i -> i.setRecipient(this));
        }
        this.reminders = reminders;
    }

    public Parent reminders(Set<Reminder> reminders) {
        this.setReminders(reminders);
        return this;
    }

    public Parent addReminders(Reminder reminder) {
        this.reminders.add(reminder);
        reminder.setRecipient(this);
        return this;
    }

    public Parent removeReminders(Reminder reminder) {
        this.reminders.remove(reminder);
        reminder.setRecipient(null);
        return this;
    }

    public Set<Feedback> getFeedbacks() {
        return this.feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        if (this.feedbacks != null) {
            this.feedbacks.forEach(i -> i.setParent(null));
        }
        if (feedbacks != null) {
            feedbacks.forEach(i -> i.setParent(this));
        }
        this.feedbacks = feedbacks;
    }

    public Parent feedbacks(Set<Feedback> feedbacks) {
        this.setFeedbacks(feedbacks);
        return this;
    }

    public Parent addFeedbacks(Feedback feedback) {
        this.feedbacks.add(feedback);
        feedback.setParent(this);
        return this;
    }

    public Parent removeFeedbacks(Feedback feedback) {
        this.feedbacks.remove(feedback);
        feedback.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parent)) {
            return false;
        }
        return getId() != null && getId().equals(((Parent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parent{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", dob='" + getDob() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}

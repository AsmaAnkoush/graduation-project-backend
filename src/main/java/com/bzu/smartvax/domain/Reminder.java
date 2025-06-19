package com.bzu.smartvax.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A Reminder.
 */
@Entity
@Table(name = "reminder")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reminder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "sent")
    private Boolean sent = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type")
    private RecipientType recipientType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "child", "schedule", "healthWorker" }, allowSetters = true)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "children", "appointments", "reminders", "feedbacks" }, allowSetters = true)
    private Parent recipient; // يمكن لاحقًا تغييره لـ Users أو entity موحد

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    @JsonIgnoreProperties(value = { "parent", "vaccinationCenter" }, allowSetters = true)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccination_center_id")
    @JsonIgnoreProperties(value = { "healthWorkers", "children" }, allowSetters = true)
    private VaccinationCenter vaccinationCenter;

    // Getters & Setters

    public VaccinationCenter getVaccinationCenter() {
        return vaccinationCenter;
    }

    public void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
        this.vaccinationCenter = vaccinationCenter;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Long getId() {
        return id;
    }

    public Reminder id(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public Reminder messageText(String messageText) {
        this.messageText = messageText;
        return this;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public RecipientType getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(RecipientType recipientType) {
        this.recipientType = recipientType;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Parent getRecipient() {
        return recipient;
    }

    public void setRecipient(Parent recipient) {
        this.recipient = recipient;
    }

    // equals, hashCode, toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reminder)) return false;
        return id != null && id.equals(((Reminder) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Reminder{" +
            "id=" +
            id +
            ", messageText='" +
            messageText +
            '\'' +
            ", scheduledDate=" +
            scheduledDate +
            ", sent=" +
            sent +
            ", recipientType=" +
            recipientType +
            '}'
        );
    }
}

package com.bzu.smartvax.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bzu.smartvax.domain.Reminder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReminderDTO implements Serializable {

    private Long id;

    @NotNull
    private String messageText;

    private AppointmentDTO appointment;

    private ParentDTO recipient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public AppointmentDTO getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentDTO appointment) {
        this.appointment = appointment;
    }

    public ParentDTO getRecipient() {
        return recipient;
    }

    public void setRecipient(ParentDTO recipient) {
        this.recipient = recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReminderDTO)) {
            return false;
        }

        ReminderDTO reminderDTO = (ReminderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reminderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReminderDTO{" +
            "id=" + getId() +
            ", messageText='" + getMessageText() + "'" +
            ", appointment=" + getAppointment() +
            ", recipient=" + getRecipient() +
            "}";
    }
}

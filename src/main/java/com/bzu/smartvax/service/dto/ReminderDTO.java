package com.bzu.smartvax.service.dto;

import com.bzu.smartvax.domain.RecipientType;
import com.bzu.smartvax.domain.ReminderType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    private LocalDateTime scheduledDate;

    private Boolean sent;

    private RecipientType recipientType;
    private String childId;
    private ReminderType type;

    private HealthWorkerDTO handledByWorker;
    private LocalDateTime handledDate;

    private Long handledByWorkerId;

    private boolean parentViewed;

    public boolean isParentViewed() {
        return parentViewed;
    }

    public void setParentViewed(boolean parentViewed) {
        this.parentViewed = parentViewed;
    }

    public Long getHandledByWorkerId() {
        return handledByWorkerId;
    }

    public void setHandledByWorkerId(Long handledByWorkerId) {
        this.handledByWorkerId = handledByWorkerId;
    }

    public HealthWorkerDTO getHandledByWorker() {
        return handledByWorker;
    }

    public void setHandledByWorker(HealthWorkerDTO handledByWorker) {
        this.handledByWorker = handledByWorker;
    }

    public LocalDateTime getHandledDate() {
        return handledDate;
    }

    public void setHandledDate(LocalDateTime handledDate) {
        this.handledDate = handledDate;
    }

    public ReminderType getType() {
        return type;
    }

    public void setType(ReminderType type) {
        this.type = type;
    }

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

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
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
            ", scheduledDate=" + getScheduledDate() +
            ", sent=" + getSent() +
            ", recipientType=" + getRecipientType() +

            "}";
    }
}

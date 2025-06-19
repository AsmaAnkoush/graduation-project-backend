package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.RecipientType;
import com.bzu.smartvax.domain.Reminder;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reminder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    // List<Reminder> findBySentFalseAndScheduledDateBefore(LocalDateTime time);

    //    @Query("SELECT r FROM Reminder r WHERE r.sent = false AND r.scheduledDate < :now")
    //    List<Reminder> findDueRemindersCustom(@Param("now") LocalDateTime now);

    boolean existsByAppointmentAndRecipientType(Appointment appointment, RecipientType recipientType);

    @Query("SELECT r FROM Reminder r WHERE r.sent = false AND r.scheduledDate < CURRENT_TIMESTAMP")
    List<Reminder> findDueRemindersCustom();

    List<Reminder> findByRecipient_Id(Long parentId);

    List<Reminder> findAllByRecipient_Id(Long recipientId);

    List<Reminder> findByRecipientType(RecipientType type);

    List<Reminder> findByRecipientTypeAndChild_IdIn(RecipientType type, List<String> childIds);

    //    boolean existsByChild_IdAndRecipientTypeAndMessageText(String childId, RecipientType recipientType, String messageText);

    boolean existsByChild_IdAndRecipientTypeAndMessageText(String childId, RecipientType type, String messageText);

    @Query(
        """
            SELECT COUNT(r) > 0
            FROM Reminder r
            WHERE r.child.id = :childId
              AND r.recipientType = :recipientType
              AND r.messageText = :messageText
        """
    )
    boolean existsCustomReminder(
        @Param("childId") String childId,
        @Param("recipientType") RecipientType recipientType,
        @Param("messageText") String messageText
    );
}

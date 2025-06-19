package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.RecipientType;
import com.bzu.smartvax.domain.Reminder;
import com.bzu.smartvax.repository.AppointmentRepository;
import com.bzu.smartvax.repository.ReminderRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReminderScheduler {

    private final ReminderService reminderService;
    private final Logger log = LoggerFactory.getLogger(ReminderScheduler.class);
    private final AppointmentRepository appointmentRepository;
    private final ReminderRepository reminderRepository;

    public ReminderScheduler(
        ReminderService reminderService,
        AppointmentRepository appointmentRepositor,
        ReminderRepository reminderRepository
    ) {
        this.reminderService = reminderService;
        this.appointmentRepository = appointmentRepositor;
        this.reminderRepository = reminderRepository;
    }

    /**
     * Scheduled task that runs every hour to send reminders.
//     */
    //    @Scheduled(cron = "0 0 * * * *") // ⏰ كل ساعة على رأس الساعة
    // @Scheduled(fixedRate = 30000) // كل 30 ثانية
    //@Transactional
    @Scheduled(fixedRate = 10000)
    public void sendScheduledReminders() {
        System.out.println("==============================================================");
        System.out.println("🔄 Scheduler is running: Checking for due reminders...");

        List<Reminder> dueReminders = reminderService.findDueReminders();

        System.out.println("==== التذكيرات المستحقة (تفاصيل): ====");
        dueReminders.forEach(r -> {
            System.out.println("➡️ ID: " + r.getId());
            System.out.println("   📆 Date: " + r.getScheduledDate());
            System.out.println("   ✅ Sent: " + r.getSent());
            System.out.println("   👤 Type: " + r.getRecipientType());
        });

        System.out.println("the size is ::: " + dueReminders.size());
        log.info("✅ تم فحص {} تذكير مستحق.", dueReminders.size());

        for (Reminder reminder : dueReminders) {
            System.out.println("📤 إرسال تذكير لـ " + reminder.getRecipientType() + ": " + reminder.getMessageText());
            reminderService.markAsSent(reminder);
        }
    }

    @Scheduled(fixedRate = 30000)
    //    @Scheduled(fixedRate = 86400000) // ⏰ كل 24 ساعة
    @Transactional
    public void createUpcomingRemindersForParents() {
        System.out.println("C...................................................................");
        System.out.println("Checking for upcoming appointments to notify parents......");
        log.info("🔄 Checking for upcoming appointments to notify parents...");

        Instant from = LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant();
        Instant to = LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()).toInstant();

        List<Appointment> upcoming = appointmentRepository.findByStatusAndAppointmentDateBetween("PENDING", from, to); // ✅ status = "PENDING"
        System.out.println("size: :::::  " + upcoming.size());
        for (Appointment app : upcoming) {
            // 🔁 تأكد ما تعمل تذكير مكرر
            if (reminderRepository.existsByAppointmentAndRecipientType(app, RecipientType.PARENT)) continue;

            Reminder r = new Reminder();
            r.setAppointment(app);
            r.setRecipient(app.getParent());
            r.setRecipientType(RecipientType.PARENT);
            r.setMessageText("🔔 تذكير: لطفلك موعد تطعيم بتاريخ " + app.getAppointmentDate().atZone(ZoneId.systemDefault()).toLocalDate());
            // r.setScheduledDate(LocalDateTime.ofInstant(app.getAppointmentDate(), ZoneId.systemDefault()).minusDays(1));

            //            Instant scheduledInstant = app.getAppointmentDate().minus(1, ChronoUnit.DAYS);
            //            r.setScheduledDate(LocalDateTime.ofInstant(scheduledInstant, ZoneId.systemDefault()));
            r.setScheduledDate(LocalDateTime.now().minusMinutes(1));
            r.setChild(app.getChild());

            r.setSent(false);

            reminderRepository.save(r);
            log.info("✅ Reminder created for parent ID {} for appointment ID {}", app.getParent().getId(), app.getId());
            System.out.println("C...................................................................");
        }
    }
}

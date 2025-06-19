package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.*;
import com.bzu.smartvax.repository.AppointmentRepository;
import com.bzu.smartvax.repository.ChildRepository;
import com.bzu.smartvax.repository.HealthWorkerRepository;
import com.bzu.smartvax.repository.ReminderRepository;
import com.bzu.smartvax.service.dto.ReminderDTO;
import com.bzu.smartvax.service.mapper.ReminderMapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import static com.bzu.smartvax.service.AppointmentService.log;

/**
 * Service Implementation for managing {@link com.bzu.smartvax.domain.Reminder}.
 */
@Service
@Transactional
public class ReminderService {

    private static final Logger LOG = LoggerFactory.getLogger(ReminderService.class);

    private final ReminderRepository reminderRepository;

    private final ReminderMapper reminderMapper;
    private final AppointmentRepository appointmentRepository;
    private final ChildRepository childRepository;
    private final HealthWorkerRepository healthWorkerRepository;

    private final Logger log = LoggerFactory.getLogger(ReminderService.class);

    public ReminderService(
        ReminderRepository reminderRepository,
        ReminderMapper reminderMapper,
        AppointmentRepository appointmentRepository,
        ChildRepository childRepository,
        HealthWorkerRepository healthWorkerRepository
    ) {
        this.reminderRepository = reminderRepository;
        this.reminderMapper = reminderMapper;
        this.appointmentRepository = appointmentRepository;
        this.childRepository = childRepository;
        this.healthWorkerRepository = healthWorkerRepository;
    }

    @Transactional(readOnly = true)
    public List<ReminderDTO> findByParentId(Long parentId) {
        return reminderRepository.findByRecipient_Id(parentId).stream().map(reminderMapper::toDto).toList();
    }

    /**
     * Save a reminder.
     *
     * @param reminderDTO the entity to save.
     * @return the persisted entity.
     */
    public ReminderDTO save(ReminderDTO reminderDTO) {
        LOG.debug("Request to save Reminder : {}", reminderDTO);
        Reminder reminder = reminderMapper.toEntity(reminderDTO);
        reminder = reminderRepository.save(reminder);
        return reminderMapper.toDto(reminder);
    }

    /**
     * Update a reminder.
     *
     * @param reminderDTO the entity to save.
     * @return the persisted entity.
     */
    public ReminderDTO update(ReminderDTO reminderDTO) {
        LOG.debug("Request to update Reminder : {}", reminderDTO);
        Reminder reminder = reminderMapper.toEntity(reminderDTO);
        reminder = reminderRepository.save(reminder);
        return reminderMapper.toDto(reminder);
    }

    /**
     * Partially update a reminder.
     *
     * @param reminderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReminderDTO> partialUpdate(ReminderDTO reminderDTO) {
        LOG.debug("Request to partially update Reminder : {}", reminderDTO);

        return reminderRepository
            .findById(reminderDTO.getId())
            .map(existingReminder -> {
                reminderMapper.partialUpdate(existingReminder, reminderDTO);

                return existingReminder;
            })
            .map(reminderRepository::save)
            .map(reminderMapper::toDto);
    }

    /**
     * Get all the reminders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReminderDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Reminders");
        return reminderRepository.findAll(pageable).map(reminderMapper::toDto);
    }

    /**
     * Get one reminder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReminderDTO> findOne(Long id) {
        LOG.debug("Request to get Reminder : {}", id);
        return reminderRepository.findById(id).map(reminderMapper::toDto);
    }

    /**
     * Delete the reminder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Reminder : {}", id);
        reminderRepository.deleteById(id);
    }

    //    @Transactional(readOnly = true)
    //    public List<Reminder> findDueReminders() {
    //        return reminderRepository.findBySentFalseAndScheduledDateBefore(LocalDateTime.now());
    //    }
    public List<Reminder> findDueReminders() {
        return reminderRepository.findDueRemindersCustom();
    }

    public void markAsSent(Reminder reminder) {
        reminder.setSent(true);
        reminderRepository.save(reminder);
    }

    public Reminder createReminder(String message, LocalDateTime sendDate, Appointment appointment, Parent recipient) {
        Reminder reminder = new Reminder();
        reminder.setMessageText(message);
        reminder.setScheduledDate(sendDate);
        reminder.setSent(false);
        reminder.setRecipientType(RecipientType.PARENT);
        reminder.setRecipient(recipient);
        reminder.setAppointment(appointment);
        return reminderRepository.save(reminder);
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void generateHealthWorkerReminders() {
        List<Object[]> missedSameVaccine = appointmentRepository.findMissedTwiceSameVaccination();
        List<Object[]> missedThreeForChild = appointmentRepository.findMissedThreeTimesForChild();

        Set<String> alreadyReminded = new HashSet<>();

        for (Object[] row : missedSameVaccine) {
            String vaccinationId = String.valueOf(row[0]);
            String childId = String.valueOf(row[1]);
            String missedAppId = String.valueOf(row[2]); // تأكد أنك تُرجع الـ appointment_id أيضًا من SQL

            String key = "vaccine:" + vaccinationId + "_child:" + childId;

            if (!alreadyReminded.contains(key)) {
                String message =
                    "⚠️ الطفل (ID: " + childId + ") فوّت نفس التطعيم مرتين! رقم التطعيم: " + vaccinationId + " | الموعد: " + missedAppId;

                boolean exists = reminderRepository.existsCustomReminder(childId, RecipientType.HEALTH_WORKER, message);

                //                boolean exists = reminderRepository.existsByChild_IdAndRecipientTypeAndMessageText(
                //                    childId, RecipientType.HEALTH_WORKER, message);

                if (!exists) {
                    createReminderForHealthWorker(childId, message);
                    alreadyReminded.add(key);
                }
            }
        }

        for (Object[] row : missedThreeForChild) {
            String childId = String.valueOf(row[0]);
            String appointmentIds = String.valueOf(row[1]); // IDs مفصولة بفواصل
            String key = "child:" + childId + "_missed3";

            if (!alreadyReminded.contains(key)) {
                String message = "⚠️ الطفل (ID: " + childId + ") فوّت 3 مواعيد مختلفة! أرقام المواعيد: " + appointmentIds;

                boolean exists = reminderRepository.existsCustomReminder(childId, RecipientType.HEALTH_WORKER, message);

                if (!exists) {
                    createReminderForHealthWorker(childId, message);
                    alreadyReminded.add(key);
                }
            }
        }
    }

    private void createReminderForHealthWorker(String childId, String message) {
        Optional<Child> childOpt = childRepository.findById(childId);
        if (childOpt.isEmpty()) return;

        Child child = childOpt.get();
        VaccinationCenter center = child.getVaccinationCenter();
        if (center == null) return;

        // ✅ فحص إذا في Reminder بنفس التفاصيل (child + message + HEALTH_WORKER)
        boolean exists = reminderRepository.existsByChild_IdAndRecipientTypeAndMessageText(childId, RecipientType.HEALTH_WORKER, message);

        if (exists) {
            log.info("⛔ Reminder already exists for child {} and message: {}", childId, message);
            return;
        }

        // ✅ إذا ما في، أنشئ Reminder واحد فقط (مش لكل health worker)
        Reminder reminder = new Reminder();
        reminder.setMessageText(message);
        reminder.setScheduledDate(LocalDateTime.now());
        reminder.setSent(false);
        reminder.setRecipientType(RecipientType.HEALTH_WORKER);
        reminder.setChild(child);
        reminder.setVaccinationCenter(center);

        reminderRepository.save(reminder);
        log.info("✅ Reminder created for child {} for message: {}", childId, message);
    }

    @Transactional(readOnly = true)
    public List<ReminderDTO> findRemindersForHealthWorkerCenter(Long workerId) {
        Optional<HealthWorker> workerOpt = healthWorkerRepository.findById(workerId);
        if (workerOpt.isEmpty()) return List.of();

        Long centerId = workerOpt.get().getVaccinationCenter().getId();

        List<Child> children = childRepository.findByVaccinationCenter_Id(centerId);

        List<String> childIds = children.stream().map(Child::getId).toList();

        return reminderRepository
            .findByRecipientTypeAndChild_IdIn(RecipientType.HEALTH_WORKER, childIds)
            .stream()
            .map(reminderMapper::toDto)
            .toList();
    }
}

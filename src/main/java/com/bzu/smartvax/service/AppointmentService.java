package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.*;
import com.bzu.smartvax.repository.*;
import com.bzu.smartvax.service.dto.AppointmentDTO;
import com.bzu.smartvax.service.mapper.AppointmentMapper;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final ChildRepository childRepository;
    private final ScheduleVaccinationRepository scheduleVaccinationRepository;
    private final UsersRepository usersRepository;
    private final HealthWorkerRepository healthWorkerRepository;

    public AppointmentService(
        AppointmentRepository appointmentRepository,
        AppointmentMapper appointmentMapper,
        ChildRepository childRepository,
        ScheduleVaccinationRepository scheduleVaccinationRepository,
        UsersRepository usersRepository,
        HealthWorkerRepository healthWorkerRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.childRepository = childRepository;
        this.scheduleVaccinationRepository = scheduleVaccinationRepository;
        this.usersRepository = usersRepository;
        this.healthWorkerRepository = healthWorkerRepository;
    }

    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        log.debug("Request to save Appointment : {}", appointmentDTO);
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    public AppointmentDTO update(AppointmentDTO dto) {
        return appointmentRepository
            .findById(dto.getId())
            .map(existing -> {
                existing.setStatus(dto.getStatus());
                existing.setAppointmentDate(dto.getAppointmentDate());
                existing.setRescheduleReason(dto.getRescheduleReason());

                if (dto.getRequestedNewCenter() != null && dto.getRequestedNewCenter().getId() != null) {
                    VaccinationCenter requestedCenter = new VaccinationCenter();
                    requestedCenter.setId(dto.getRequestedNewCenter().getId());
                    existing.setRequestedNewCenter(requestedCenter);
                } else {
                    existing.setRequestedNewCenter(null);
                }

                return appointmentMapper.toDto(appointmentRepository.save(existing));
            })
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id " + dto.getId()));
    }

    public Optional<AppointmentDTO> partialUpdate(AppointmentDTO appointmentDTO) {
        log.debug("Request to partially update Appointment : {}", appointmentDTO);

        return appointmentRepository
            .findById(appointmentDTO.getId())
            .map(existingAppointment -> {
                appointmentMapper.partialUpdate(existingAppointment, appointmentDTO);
                return existingAppointment;
            })
            .map(appointmentRepository::save)
            .map(appointmentMapper::toDto);
    }

    @Transactional
    public Page<AppointmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Appointments");
        return appointmentRepository.findAll(pageable).map(appointmentMapper::toDto);
    }

    @Transactional
    public AppointmentDTO acceptLocationChange(Long appointmentId) {
        Appointment appointment = appointmentRepository
            .findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id " + appointmentId));

        if (!"trlocation".equalsIgnoreCase(appointment.getStatus())) {
            throw new IllegalStateException("Appointment is not in location change request status");
        }

        // المركز الصحي الجديد المطلوب
        VaccinationCenter newCenter = appointment.getRequestedNewCenter();
        if (newCenter == null) {
            throw new IllegalStateException("Requested new center is null");
        }

        // تحديث مركز التطعيم للطفل
        Child child = appointment.getChild();
        child.setVaccinationCenter(newCenter);
        childRepository.save(child);

        // تحديث مركز التطعيم في الموعد نفسه
        appointment.setVaccinationCenter(newCenter);

        // تحديث حالة الموعد وتنظيف طلب تغيير المركز
        appointment.setStatus("confirmed");
        appointment.setRequestedNewCenter(null);
        appointmentRepository.save(appointment);

        return appointmentMapper.toDto(appointment);
    }

    @Scheduled(fixedRate = 30000) // كل دقيقة
    @Transactional
    public void markMissedAppointmentsAutomatically() {
        LocalDate today = LocalDate.now();

        List<Appointment> appointments = appointmentRepository.findAll();
        for (Appointment a : appointments) {
            if (!List.of("completed", "cancelled", "missed").contains(a.getStatus().toLowerCase())) {
                LocalDate date = a.getAppointmentDate().atZone(ZoneId.systemDefault()).toLocalDate();
                if (date.isBefore(today)) {
                    // ✅ تأكد من وجود كل العلاقات الضرورية
                    if (a.getChild() == null || a.getSchedules() == null || a.getSchedules().isEmpty()) {
                        log.warn("⚠️ الموعد {} لا يحتوي على بيانات كافية (child/schedules) لتعيينه كـ missed", a.getId());
                        continue;
                    }

                    a.setStatus("missed");
                    appointmentRepository.save(a);
                    log.info("🚨 تم تعيين الموعد {} كـ missed لتاريخه: {}", a.getId(), date);
                }
            }
        }
    }

    @Transactional
    public AppointmentDTO markAppointmentAsCompleted(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository
            .findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id " + appointmentId));

        if ("completed".equalsIgnoreCase(appointment.getStatus())) {
            throw new IllegalStateException("Appointment is already completed");
        }

        if ("cancelled".equalsIgnoreCase(appointment.getStatus())) {
            throw new IllegalStateException("Cancelled appointment cannot be marked as completed");
        }

        // جلب المستخدم
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!"HEALTH_WORKER".equalsIgnoreCase(user.getRole())) {
            throw new IllegalStateException("User is not a health worker");
        }

        // جلب العامل الصحي
        Long healthWorkerId = user.getReferenceId();
        HealthWorker hw = healthWorkerRepository
            .findById(healthWorkerId)
            .orElseThrow(() -> new IllegalArgumentException("Health worker not found"));

        // تحديث الموعد
        appointment.setStatus("completed");
        appointment.setHealthWorker(hw);

        Appointment updated = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updated);
    }

    @Transactional
    public AppointmentDTO acceptReschedule(Long appointmentId, LocalDate newDate) {
        Appointment appointment = appointmentRepository
            .findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (!"reshdualing".equalsIgnoreCase(appointment.getStatus())) {
            throw new IllegalStateException("Appointment is not in reschedule request status");
        }

        // تحديث الموعد
        Instant newDateInstant = newDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        appointment.setAppointmentDate(newDateInstant);
        appointment.setStatus("confirmed");
        appointment.setRescheduleReason(null);
        appointmentRepository.save(appointment);

        // نبحث عن أي ScheduleVaccination مرتبط بهذا الموعد
        Optional<ScheduleVaccination> anyScheduled = appointment.getSchedules().stream().findFirst();

        if (anyScheduled.isPresent()) {
            ScheduleVaccination sv = anyScheduled.get();
            VaccinationGroup group = sv.getVaccination().getGroup();
            int threshold = group.getShiftThresholdDays();

            LocalDate original = sv.getScheduledDate();
            long diff = ChronoUnit.DAYS.between(original, newDate);

            if (Math.abs(diff) > threshold) {
                // نؤجل كل المواعيد القادمة للطفل
                Child child = appointment.getChild();
                List<ScheduleVaccination> future = scheduleVaccinationRepository.findByChild_IdAndScheduledDateAfter(
                    child.getId(),
                    LocalDate.now()
                );

                for (ScheduleVaccination s : future) {
                    s.setScheduledDate(s.getScheduledDate().plusDays(7));
                    scheduleVaccinationRepository.save(s);
                }
            }
        }

        return appointmentMapper.toDto(appointment);
    }

    @Transactional
    public AppointmentDTO rejectRequest(Long appointmentId) {
        Appointment appointment = appointmentRepository
            .findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id " + appointmentId));

        // إلغاء طلب التأجيل أو تغيير الموقع
        appointment.setStatus("pending");
        appointment.setRequestedNewCenter(null);
        appointment.setRescheduleReason(null);
        appointmentRepository.save(appointment);

        return appointmentMapper.toDto(appointment);
    }

    public Optional<AppointmentDTO> findOne(Long id) {
        log.debug("Request to get Appointment : {}", id);
        return appointmentRepository.findById(id).map(appointmentMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete Appointment : {}", id);
        appointmentRepository.deleteById(id);
    }

    //    @Scheduled(cron = "0 0 1 * * *") // كل يوم الساعة 1 صباحًا
    //    @Transactional
    //    public void autoCancelOverdueAppointments() {
    //        LocalDate today = LocalDate.now();
    //
    //        List<Appointment> all = appointmentRepository.findAll();
    //        for (Appointment a : all) {
    //            if (!"completed".equalsIgnoreCase(a.getStatus())) {
    //                LocalDate date = a.getAppointmentDate().atZone(ZoneId.systemDefault()).toLocalDate();
    //                if (date.isBefore(today)) {
    //                    a.setStatus("cancelled");
    //                    appointmentRepository.save(a);
    //                }
    //            }
    //        }
    //    }

    @Scheduled(fixedRate = 30000)
    public void autoAssignAppointments() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Jerusalem"));
        List<ScheduleVaccination> schedules = scheduleVaccinationRepository.findAll();

        var grouped = schedules
            .stream()
            .collect(
                Collectors.groupingBy(sv -> sv.getScheduledDate() + "_" + sv.getVaccinationGroup().getId() + "_" + sv.getChild().getId())
            );

        for (Map.Entry<String, List<ScheduleVaccination>> entry : grouped.entrySet()) {
            List<ScheduleVaccination> group = entry.getValue();
            ScheduleVaccination example = group.get(0);
            Child child = example.getChild();
            LocalDate scheduledDate = example.getScheduledDate();

            // معالجة الإجازات أولاً (إذا صادف يوم الجمعة أو السبت، انقل إلى الأحد)
            if (scheduledDate.getDayOfWeek().getValue() == 5 || scheduledDate.getDayOfWeek().getValue() == 6) {
                scheduledDate = scheduledDate.with(java.time.temporal.TemporalAdjusters.next(java.time.DayOfWeek.SUNDAY));
            }
            LocalDate finalDate = scheduledDate;

            List<Appointment> appointments = appointmentRepository.findByChild_Id(child.getId());

            // تحديد الحالة حسب تاريخ الموعد بالنسبة لليوم
            String status;
            if (scheduledDate.isBefore(today)) {
                status = "missed";
            } else if (!scheduledDate.isAfter(today.plusDays(7))) {
                status = "PENDING";
            } else {
                continue; // تجاهل المواعيد البعيدة
            }

            // التحقق من وجود موعد بنفس الطفل، نفس التاريخ ونفس مجموعة التطعيم (بغض النظر عن الحالة)
            boolean exists = appointments
                .stream()
                .anyMatch(app -> {
                    boolean sameDate = app.getAppointmentDate().atZone(ZoneId.of("Asia/Jerusalem")).toLocalDate().equals(finalDate);

                    boolean sameGroup = app
                        .getSchedules()
                        .stream()
                        .map(sv -> sv.getVaccination().getGroup().getId())
                        .collect(Collectors.toSet())
                        .equals(group.stream().map(sv -> sv.getVaccination().getGroup().getId()).collect(Collectors.toSet()));

                    return sameDate && sameGroup;
                });

            if (exists) {
                continue; // لا تنشئ موعد مكرر بنفس التاريخ والمجموعة بغض النظر عن الحالة
            }

            // إعداد الوقت النهائي للموعد (الثامنة صباحاً بتوقيت القدس)
            Instant finalAppointmentDate = scheduledDate.atTime(8, 0).atZone(ZoneId.of("Asia/Jerusalem")).toInstant();

            Appointment appointment = new Appointment();
            appointment.setAppointmentDate(finalAppointmentDate);
            appointment.setChild(child);
            appointment.setParent(child.getParent());
            appointment.setVaccinationCenter(child.getVaccinationCenter());
            appointment.setHealthWorker(null);
            appointment.setSchedules(group);
            appointment.setStatus(status);

            appointmentRepository.save(appointment);
            log.info("✅ تم إنشاء موعد للطفل {} في التاريخ {} بالحالة {}", child.getId(), scheduledDate, status);
        }
    }
}

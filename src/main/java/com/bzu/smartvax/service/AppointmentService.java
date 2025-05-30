package com.bzu.smartvax.service;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.domain.ScheduleVaccination;
import com.bzu.smartvax.domain.VaccinationCenter;
import com.bzu.smartvax.repository.AppointmentRepository;
import com.bzu.smartvax.repository.ChildRepository;
import com.bzu.smartvax.repository.ScheduleVaccinationRepository;
import com.bzu.smartvax.service.dto.AppointmentDTO;
import com.bzu.smartvax.service.mapper.AppointmentMapper;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
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

    public AppointmentService(
        AppointmentRepository appointmentRepository,
        AppointmentMapper appointmentMapper,
        ChildRepository childRepository,
        ScheduleVaccinationRepository scheduleVaccinationRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.childRepository = childRepository;
        this.scheduleVaccinationRepository = scheduleVaccinationRepository;
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

    public Optional<AppointmentDTO> findOne(Long id) {
        log.debug("Request to get Appointment : {}", id);
        return appointmentRepository.findById(id).map(appointmentMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete Appointment : {}", id);
        appointmentRepository.deleteById(id);
    }

    @Scheduled(fixedRate = 30000)
    public void autoAssignAppointments() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Jerusalem"));

        List<ScheduleVaccination> schedules = scheduleVaccinationRepository.findAll();

        List<ScheduleVaccination> dueSchedules = schedules
            .stream()
            .filter(sv -> {
                LocalDate scheduledDate = sv.getScheduledDate();
                LocalDate weekBefore = scheduledDate.minusDays(7);
                LocalDate weekAfter = scheduledDate.plusDays(7);
                return (today.isEqual(weekBefore) || today.isAfter(scheduledDate)) && today.isBefore(weekAfter);
            })
            .toList();

        var grouped = dueSchedules
            .stream()
            .collect(
                java.util.stream.Collectors.groupingBy(
                    sv -> sv.getScheduledDate() + "_" + sv.getVaccinationGroup().getId() + "_" + sv.getChild().getId()
                )
            );

        for (List<ScheduleVaccination> group : grouped.values()) {
            ScheduleVaccination example = group.get(0);
            Child child = example.getChild();
            LocalDate scheduledDate = example.getScheduledDate();

            // معالجة الإجازات: إذا الجمعة أو السبت → الأحد التالي
            if (scheduledDate.getDayOfWeek().getValue() == 5 || scheduledDate.getDayOfWeek().getValue() == 6) {
                scheduledDate = scheduledDate.with(java.time.temporal.TemporalAdjusters.next(java.time.DayOfWeek.SUNDAY));
            }

            Instant appointmentInstant = scheduledDate.atStartOfDay(ZoneId.of("Asia/Jerusalem")).toInstant();

            boolean alreadyExists = appointmentRepository.existsByChildAndAppointmentDateAndStatusIn(
                child,
                appointmentInstant,
                List.of("PENDING", "reshdualing", "confirmed", "trlocation", "completed")
            );

            if (!alreadyExists) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentDate(appointmentInstant);
                appointment.setChild(child);
                appointment.setParent(child.getParent());
                appointment.setStatus("PENDING");
                appointment.setVaccinationCenter(child.getVaccinationCenter());
                appointment.setHealthWorker(null);
                appointment.setSchedules(group); // ربط الموعد بالتطعيمات الفعلية

                appointmentRepository.save(appointment);
                log.info("✅ موعد جديد للطفل {} بتاريخ {}", child.getId(), scheduledDate);
            }
        }
    }
}

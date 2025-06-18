package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.HealthWorker;
import com.bzu.smartvax.domain.Users;
import com.bzu.smartvax.repository.AppointmentRepository;
import com.bzu.smartvax.repository.HealthWorkerRepository;
import com.bzu.smartvax.repository.UsersRepository;
import com.bzu.smartvax.service.AppointmentService;
import com.bzu.smartvax.service.dto.AppointmentDTO;
import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import com.bzu.smartvax.service.mapper.AppointmentMapper;
import com.bzu.smartvax.web.rest.errors.BadRequestAlertException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(AppointmentResource.class);

    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UsersRepository usersRepository;
    private final HealthWorkerRepository healthWorkerRepository;

    public AppointmentResource(
        AppointmentService appointmentService,
        AppointmentRepository appointmentRepository,
        AppointmentMapper appointmentMapper,
        UsersRepository usersRepository,
        HealthWorkerRepository healthWorkerRepository
    ) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.usersRepository = usersRepository;
        this.healthWorkerRepository = healthWorkerRepository;
    }

    @GetMapping("/by-center/{centerId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByCenter(@PathVariable Long centerId) {
        List<Appointment> appointments = appointmentRepository.findByChild_VaccinationCenter_Id(centerId);
        List<AppointmentDTO> dtoList = appointments.stream().map(appointmentMapper::toDto).toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/by-parent/{parentId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByParentId(@PathVariable Long parentId) {
        List<Appointment> appointments = appointmentRepository.findByParent_Id(parentId);
        List<AppointmentDTO> dtoList = appointments.stream().map(appointmentMapper::toDto).toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/by-parent-with-schedules/{parentId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByParentWithSchedules(@PathVariable Long parentId) {
        log.debug("🎯 Enter: getAppointmentsByParentWithSchedules() with parentId = {}", parentId);

        List<Appointment> appointments = appointmentRepository.findByParentIdWithSchedules(parentId);
        List<AppointmentDTO> dtoList = appointments.stream().map(appointmentMapper::toDto).toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/parent-id/by-user/{userId}")
    public ResponseEntity<Long> getParentIdByUserId(@PathVariable Long userId) {
        return usersRepository
            .findById(userId)
            .filter(user -> "PARENT".equalsIgnoreCase(user.getRole()))
            .map(user -> ResponseEntity.ok(user.getReferenceId()))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentDTO appointmentDTO) {
        if (appointmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "appointment", "idnull");
        }
        if (!Objects.equals(id, appointmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", "appointment", "idinvalid");
        }

        if (!appointmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", "appointment", "idnotfound");
        }

        AppointmentDTO result = appointmentService.update(appointmentDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/by-center-with-details/{centerId}")
    public List<AppointmentDTO> getAppointmentsByCenterWithDetails(@PathVariable Long centerId) {
        return appointmentRepository.findByChild_VaccinationCenter_Id(centerId).stream().map(appointmentMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        Optional<AppointmentDTO> appointmentDTO = appointmentService.findOne(id);
        return appointmentDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/accept-location-change")
    public ResponseEntity<AppointmentDTO> acceptLocationChange(@PathVariable Long id) {
        try {
            AppointmentDTO updated = appointmentService.acceptLocationChange(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{id}/mark-completed")
    public ResponseEntity<?> markCompleted(
        @PathVariable Long id,
        @RequestParam(name = "userId") Long userId // ✅ مهم جدًا
    ) {
        try {
            AppointmentDTO updated = appointmentService.markAppointmentAsCompleted(id, userId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/accept-reschedule")
    public ResponseEntity<?> acceptReschedule(
        @PathVariable Long id,
        @RequestParam("newDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDate
    ) {
        try {
            AppointmentDTO updated = appointmentService.acceptReschedule(id, newDate);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PatchMapping("/{id}/reject-request")
    public ResponseEntity<AppointmentDTO> rejectRequest(@PathVariable Long id) {
        try {
            AppointmentDTO updated = appointmentService.rejectRequest(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/appointments/for-current-parent")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForCurrentParent(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null || !"PARENT".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long parentId = user.getReferenceId();
        List<Appointment> appointments = appointmentRepository.findByParent_Id(parentId);
        List<AppointmentDTO> dtoList = appointments.stream().map(appointmentMapper::toDto).toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/health-worker/{userId}/appointments-by-date")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByCenterAndDate(
        @PathVariable("userId") Long userId,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LOG.info("📩 Received request for userId: {}, date: {}", userId, date);

        // 1. جلب المستخدم والتأكد من أنه عامل صحي
        Optional<Users> userOpt = usersRepository.findById(userId);
        if (userOpt.isEmpty() || !"HEALTH_WORKER".equalsIgnoreCase(userOpt.get().getRole())) {
            LOG.warn("🚫 Invalid user or role");
            return ResponseEntity.badRequest().build();
        }

        Long healthWorkerId = userOpt.get().getReferenceId();
        if (healthWorkerId == null) {
            LOG.warn("🚫 Reference ID is null");
            return ResponseEntity.badRequest().build();
        }

        // 2. جلب العامل الصحي وتأكيد وجود المركز الصحي
        Optional<HealthWorker> hwOpt = healthWorkerRepository.findById(healthWorkerId);
        if (hwOpt.isEmpty() || hwOpt.get().getVaccinationCenter() == null) {
            LOG.warn("🚫 Health worker or center not found");
            return ResponseEntity.notFound().build();
        }

        Long centerId = hwOpt.get().getVaccinationCenter().getId();
        LOG.info("✅ Vaccination center ID: {}", centerId);

        // 3. تجهيز التاريخ
        Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // 4. جلب المواعيد من جدول appointment
        List<Appointment> appointments = appointmentRepository.findByVaccinationCenterIdAndAppointmentDateBetween(
            centerId,
            startOfDay,
            endOfDay
        );

        LOG.info("📅 Found {} appointments", appointments.size());

        // 5. تحويل إلى DTO
        List<AppointmentDTO> dtos = appointments.stream().map(appointmentMapper::toDto).toList();

        return ResponseEntity.ok(dtos);
    }
}

package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.Users;
import com.bzu.smartvax.repository.AppointmentRepository;
import com.bzu.smartvax.repository.UsersRepository;
import com.bzu.smartvax.service.AppointmentService;
import com.bzu.smartvax.service.dto.AppointmentDTO;
import com.bzu.smartvax.service.dto.ChildDTO;
import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import com.bzu.smartvax.service.mapper.AppointmentMapper;
import com.bzu.smartvax.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bzu.smartvax.domain.Appointment}.
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentResource.class);

    private static final String ENTITY_NAME = "appointment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppointmentService appointmentService;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    private final UsersRepository usersRepository;

    public AppointmentResource(
        AppointmentService appointmentService,
        AppointmentRepository appointmentRepository,
        AppointmentMapper appointmentMapper,
        UsersRepository usersRepository
    ) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.usersRepository = usersRepository;
    }

    @PostMapping("")
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) throws URISyntaxException {
        LOG.debug("REST request to save Appointment : {}", appointmentDTO);
        if (appointmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new appointment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appointmentDTO = appointmentService.save(appointmentDTO);
        return ResponseEntity.created(new URI("/api/appointments/" + appointmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appointmentDTO.getId().toString()))
            .body(appointmentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppointmentDTO appointmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Appointment : {}, {}", id, appointmentDTO);
        if (appointmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appointmentDTO = appointmentService.update(appointmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentDTO.getId().toString()))
            .body(appointmentDTO);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppointmentDTO> partialUpdateAppointment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppointmentDTO appointmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Appointment partially : {}, {}", id, appointmentDTO);
        if (appointmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppointmentDTO> result = appointmentService.partialUpdate(appointmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Appointments");
        Page<AppointmentDTO> page = appointmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Appointment : {}", id);
        Optional<AppointmentDTO> appointmentDTO = appointmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appointmentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Appointment : {}", id);
        appointmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/by-center/{centerId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByCenter(@PathVariable Long centerId) {
        List<Appointment> appointments = appointmentRepository.findByChild_VaccinationCenter_Id(centerId);
        List<AppointmentDTO> dtos = appointments.stream().map(appointmentMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/by-parent/{parentId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByParent(@PathVariable Long parentId) {
        List<Appointment> appointments = appointmentRepository.findByParent_Id(parentId);
        List<AppointmentDTO> dtos = appointments.stream().map(appointmentMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/parent-id/by-user/{userId}")
    public ResponseEntity<Long> getParentIdByUserId(@PathVariable Long userId) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent() && "PARENT".equalsIgnoreCase(user.get().getRole())) {
            return ResponseEntity.ok(user.get().getReferenceId());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/health-worker/{id}/appointments-by-date")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDate(
        @PathVariable("id") Long healthWorkerId,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Appointment> appointments = appointmentRepository.findByHealthWorkerIdAndAppointmentDateBetween(
            healthWorkerId,
            startOfDay,
            endOfDay
        );

        List<AppointmentDTO> dtos = appointments
            .stream()
            .map(app -> {
                AppointmentDTO dto = appointmentMapper.toDto(app);

                List<ScheduleVaccinationDTO> scheduleDTOs = app
                    .getSchedules()
                    .stream()
                    .map(schedule -> {
                        ScheduleVaccinationDTO sDto = new ScheduleVaccinationDTO();
                        sDto.setId(schedule.getId());
                        sDto.setScheduledDate(schedule.getScheduledDate());
                        sDto.setStatus(schedule.getStatus());

                        if (schedule.getVaccination() != null) {
                            VaccinationDTO vDto = new VaccinationDTO();
                            vDto.setId(schedule.getVaccination().getId());
                            vDto.setName(schedule.getVaccination().getName());
                            sDto.setVaccination(vDto);
                        }

                        return sDto;
                    })
                    .toList();

                dto.setScheduleVaccinations(scheduleDTOs);

                return dto;
            })
            .toList();

        return ResponseEntity.ok(dtos);
    }
}

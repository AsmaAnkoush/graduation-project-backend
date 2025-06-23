package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.domain.*;
import com.bzu.smartvax.repository.HealthWorkerRepository;
import com.bzu.smartvax.repository.ParentRepository;
import com.bzu.smartvax.repository.ReminderRepository;
import com.bzu.smartvax.service.ReminderService;
import com.bzu.smartvax.service.dto.ReminderDTO;
import com.bzu.smartvax.service.mapper.ReminderMapper;
import com.bzu.smartvax.web.rest.errors.BadRequestAlertException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bzu.smartvax.domain.Reminder}.
 */
@RestController
@RequestMapping("/api/reminders")
public class ReminderResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReminderResource.class);

    private static final String ENTITY_NAME = "reminder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReminderService reminderService;

    private final ReminderRepository reminderRepository;

    private final ParentRepository parentRepository;
    private final ReminderMapper reminderMapper;
    private final HealthWorkerRepository healthWorkerRepository;

    public ReminderResource(
        ReminderService reminderService,
        ReminderRepository reminderRepository,
        ParentRepository parentRepository,
        ReminderMapper reminderMapper,
        HealthWorkerRepository healthWorkerRepository
    ) {
        this.reminderService = reminderService;
        this.reminderRepository = reminderRepository;
        this.parentRepository = parentRepository;
        this.reminderMapper = reminderMapper;
        this.healthWorkerRepository = healthWorkerRepository;
    }

    /**
     * {@code POST  /reminders} : Create a new reminder.
     *
     * @param reminderDTO the reminderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reminderDTO, or with status {@code 400 (Bad Request)} if the reminder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReminderDTO> createReminder(@Valid @RequestBody ReminderDTO reminderDTO) throws URISyntaxException {
        LOG.debug("REST request to save Reminder : {}", reminderDTO);
        if (reminderDTO.getId() != null) {
            throw new BadRequestAlertException("A new reminder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reminderDTO = reminderService.save(reminderDTO);
        return ResponseEntity.created(new URI("/api/reminders/" + reminderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reminderDTO.getId().toString()))
            .body(reminderDTO);
    }

    /**
     * {@code PUT  /reminders/:id} : Updates an existing reminder.
     *
     * @param id the id of the reminderDTO to save.
     * @param reminderDTO the reminderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reminderDTO,
     * or with status {@code 400 (Bad Request)} if the reminderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reminderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReminderDTO> updateReminder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReminderDTO reminderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Reminder : {}, {}", id, reminderDTO);
        if (reminderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reminderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reminderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reminderDTO = reminderService.update(reminderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reminderDTO.getId().toString()))
            .body(reminderDTO);
    }

    /**
     * {@code PATCH  /reminders/:id} : Partial updates given fields of an existing reminder, field will ignore if it is null
     *
     * @param id the id of the reminderDTO to save.
     * @param reminderDTO the reminderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reminderDTO,
     * or with status {@code 400 (Bad Request)} if the reminderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reminderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reminderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReminderDTO> partialUpdateReminder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReminderDTO reminderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Reminder partially : {}, {}", id, reminderDTO);
        if (reminderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reminderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reminderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReminderDTO> result = reminderService.partialUpdate(reminderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reminderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reminders} : get all the reminders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reminders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReminderDTO>> getAllReminders(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Reminders");
        Page<ReminderDTO> page = reminderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reminders/:id} : get the "id" reminder.
     *
     * @param id the id of the reminderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reminderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReminderDTO> getReminder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Reminder : {}", id);
        Optional<ReminderDTO> reminderDTO = reminderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reminderDTO);
    }

    /**
     * {@code DELETE  /reminders/:id} : delete the "id" reminder.
     *
     * @param id the id of the reminderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Reminder : {}", id);
        reminderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/by-session")
    public ResponseEntity<List<ReminderDTO>> getRemindersForLoggedParent(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        Optional<Parent> parentOpt = parentRepository.findByUser_Id(userId);
        if (parentOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Reminder> reminders = reminderRepository.findAllByRecipient_Id(parentOpt.get().getId());
        return ResponseEntity.ok(reminderMapper.toDto(reminders));
    }

    @GetMapping("/reminders/by-type/{type}")
    public ResponseEntity<List<ReminderDTO>> getRemindersByRecipientType(@PathVariable RecipientType type) {
        List<ReminderDTO> list = reminderRepository.findByRecipientType(type).stream().map(reminderMapper::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/reminders/by-worker-center/{workerId}")
    public ResponseEntity<List<ReminderDTO>> getRemindersByWorkerCenter(@PathVariable Long workerId) {
        List<ReminderDTO> list = reminderService.findRemindersForHealthWorkerCenter(workerId);
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/mark-handled/{id}")
    public ResponseEntity<Void> markHandled(@PathVariable Long id, HttpSession session) {
        Users user = (Users) session.getAttribute("user");

        if (user == null || !"HEALTH_WORKER".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long healthWorkerId = user.getReferenceId(); // مفتاح الهيلث ووركر المرتبط باليوزر

        Optional<Reminder> optionalReminder = reminderRepository.findWithChildAndCenterById(id);
        if (optionalReminder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reminder reminder = optionalReminder.get();

        // تحقق إن مركز التطعيم للطفل نفسه الموجود في حساب العامل الصحي
        Optional<HealthWorker> optionalHW = healthWorkerRepository.findById(healthWorkerId);
        if (optionalHW.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HealthWorker hw = optionalHW.get();
        if (!reminder.getChild().getVaccinationCenter().getId().equals(hw.getVaccinationCenter().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // حفظ من تعامل مع التذكير وتاريخ التعامل
        reminder.setHandledByWorker(hw);
        reminder.setHandledDate(LocalDateTime.now()); // فقط التاريخ بدون الوقت
        reminderRepository.save(reminder);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/mark-parent-viewed/{id}")
    public ResponseEntity<Void> markParentViewed(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Parent> parentOpt = parentRepository.findByUser_Id(userId);
        if (parentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Reminder> optionalReminder = reminderRepository.findById(id);
        if (optionalReminder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reminder reminder = optionalReminder.get();

        // تحقق أن التذكير فعليًا موجه لهذا الأب
        if (!reminder.getRecipient().getId().equals(parentOpt.get().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // علّمه كمقروء
        reminder.setParentViewed(true);
        reminderRepository.save(reminder);

        return ResponseEntity.ok().build();
    }
}

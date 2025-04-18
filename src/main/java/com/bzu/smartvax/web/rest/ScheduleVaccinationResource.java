package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.repository.ScheduleVaccinationRepository;
import com.bzu.smartvax.service.ScheduleVaccinationService;
import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import com.bzu.smartvax.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bzu.smartvax.domain.ScheduleVaccination}.
 */
@RestController
@RequestMapping("/api/schedule-vaccinations")
public class ScheduleVaccinationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleVaccinationResource.class);

    private static final String ENTITY_NAME = "scheduleVaccination";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduleVaccinationService scheduleVaccinationService;

    private final ScheduleVaccinationRepository scheduleVaccinationRepository;

    public ScheduleVaccinationResource(
        ScheduleVaccinationService scheduleVaccinationService,
        ScheduleVaccinationRepository scheduleVaccinationRepository
    ) {
        this.scheduleVaccinationService = scheduleVaccinationService;
        this.scheduleVaccinationRepository = scheduleVaccinationRepository;
    }

    /**
     * {@code POST  /schedule-vaccinations} : Create a new scheduleVaccination.
     *
     * @param scheduleVaccinationDTO the scheduleVaccinationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduleVaccinationDTO, or with status {@code 400 (Bad Request)} if the scheduleVaccination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScheduleVaccinationDTO> createScheduleVaccination(
        @Valid @RequestBody ScheduleVaccinationDTO scheduleVaccinationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ScheduleVaccination : {}", scheduleVaccinationDTO);
        if (scheduleVaccinationDTO.getId() != null) {
            throw new BadRequestAlertException("A new scheduleVaccination cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scheduleVaccinationDTO = scheduleVaccinationService.save(scheduleVaccinationDTO);
        return ResponseEntity.created(new URI("/api/schedule-vaccinations/" + scheduleVaccinationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, scheduleVaccinationDTO.getId().toString()))
            .body(scheduleVaccinationDTO);
    }

    /**
     * {@code PUT  /schedule-vaccinations/:id} : Updates an existing scheduleVaccination.
     *
     * @param id the id of the scheduleVaccinationDTO to save.
     * @param scheduleVaccinationDTO the scheduleVaccinationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleVaccinationDTO,
     * or with status {@code 400 (Bad Request)} if the scheduleVaccinationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduleVaccinationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleVaccinationDTO> updateScheduleVaccination(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScheduleVaccinationDTO scheduleVaccinationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ScheduleVaccination : {}, {}", id, scheduleVaccinationDTO);
        if (scheduleVaccinationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleVaccinationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleVaccinationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scheduleVaccinationDTO = scheduleVaccinationService.update(scheduleVaccinationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scheduleVaccinationDTO.getId().toString()))
            .body(scheduleVaccinationDTO);
    }

    /**
     * {@code PATCH  /schedule-vaccinations/:id} : Partial updates given fields of an existing scheduleVaccination, field will ignore if it is null
     *
     * @param id the id of the scheduleVaccinationDTO to save.
     * @param scheduleVaccinationDTO the scheduleVaccinationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleVaccinationDTO,
     * or with status {@code 400 (Bad Request)} if the scheduleVaccinationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the scheduleVaccinationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduleVaccinationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduleVaccinationDTO> partialUpdateScheduleVaccination(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScheduleVaccinationDTO scheduleVaccinationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ScheduleVaccination partially : {}, {}", id, scheduleVaccinationDTO);
        if (scheduleVaccinationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleVaccinationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleVaccinationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduleVaccinationDTO> result = scheduleVaccinationService.partialUpdate(scheduleVaccinationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scheduleVaccinationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /schedule-vaccinations} : get all the scheduleVaccinations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduleVaccinations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ScheduleVaccinationDTO>> getAllScheduleVaccinations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ScheduleVaccinations");
        Page<ScheduleVaccinationDTO> page = scheduleVaccinationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /schedule-vaccinations/:id} : get the "id" scheduleVaccination.
     *
     * @param id the id of the scheduleVaccinationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduleVaccinationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleVaccinationDTO> getScheduleVaccination(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ScheduleVaccination : {}", id);
        Optional<ScheduleVaccinationDTO> scheduleVaccinationDTO = scheduleVaccinationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scheduleVaccinationDTO);
    }

    /**
     * {@code DELETE  /schedule-vaccinations/:id} : delete the "id" scheduleVaccination.
     *
     * @param id the id of the scheduleVaccinationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduleVaccination(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ScheduleVaccination : {}", id);
        scheduleVaccinationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

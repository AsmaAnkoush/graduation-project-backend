package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.repository.HealthWorkerRepository;
import com.bzu.smartvax.service.HealthWorkerService;
import com.bzu.smartvax.service.dto.HealthWorkerDTO;
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
 * REST controller for managing {@link com.bzu.smartvax.domain.HealthWorker}.
 */
@RestController
@RequestMapping("/api/health-workers")
public class HealthWorkerResource {

    private static final Logger LOG = LoggerFactory.getLogger(HealthWorkerResource.class);

    private static final String ENTITY_NAME = "healthWorker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HealthWorkerService healthWorkerService;

    private final HealthWorkerRepository healthWorkerRepository;

    public HealthWorkerResource(HealthWorkerService healthWorkerService, HealthWorkerRepository healthWorkerRepository) {
        this.healthWorkerService = healthWorkerService;
        this.healthWorkerRepository = healthWorkerRepository;
    }

    /**
     * {@code POST  /health-workers} : Create a new healthWorker.
     *
     * @param healthWorkerDTO the healthWorkerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new healthWorkerDTO, or with status {@code 400 (Bad Request)} if the healthWorker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HealthWorkerDTO> createHealthWorker(@Valid @RequestBody HealthWorkerDTO healthWorkerDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HealthWorker : {}", healthWorkerDTO);
        if (healthWorkerDTO.getId() != null) {
            throw new BadRequestAlertException("A new healthWorker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        healthWorkerDTO = healthWorkerService.save(healthWorkerDTO);
        return ResponseEntity.created(new URI("/api/health-workers/" + healthWorkerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, healthWorkerDTO.getId().toString()))
            .body(healthWorkerDTO);
    }

    /**
     * {@code PUT  /health-workers/:id} : Updates an existing healthWorker.
     *
     * @param id the id of the healthWorkerDTO to save.
     * @param healthWorkerDTO the healthWorkerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated healthWorkerDTO,
     * or with status {@code 400 (Bad Request)} if the healthWorkerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the healthWorkerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HealthWorkerDTO> updateHealthWorker(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HealthWorkerDTO healthWorkerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HealthWorker : {}, {}", id, healthWorkerDTO);
        if (healthWorkerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, healthWorkerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!healthWorkerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        healthWorkerDTO = healthWorkerService.update(healthWorkerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, healthWorkerDTO.getId().toString()))
            .body(healthWorkerDTO);
    }

    /**
     * {@code PATCH  /health-workers/:id} : Partial updates given fields of an existing healthWorker, field will ignore if it is null
     *
     * @param id the id of the healthWorkerDTO to save.
     * @param healthWorkerDTO the healthWorkerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated healthWorkerDTO,
     * or with status {@code 400 (Bad Request)} if the healthWorkerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the healthWorkerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the healthWorkerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HealthWorkerDTO> partialUpdateHealthWorker(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HealthWorkerDTO healthWorkerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HealthWorker partially : {}, {}", id, healthWorkerDTO);
        if (healthWorkerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, healthWorkerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!healthWorkerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HealthWorkerDTO> result = healthWorkerService.partialUpdate(healthWorkerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, healthWorkerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /health-workers} : get all the healthWorkers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of healthWorkers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HealthWorkerDTO>> getAllHealthWorkers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of HealthWorkers");
        Page<HealthWorkerDTO> page = healthWorkerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /health-workers/:id} : get the "id" healthWorker.
     *
     * @param id the id of the healthWorkerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the healthWorkerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HealthWorkerDTO> getHealthWorker(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HealthWorker : {}", id);
        Optional<HealthWorkerDTO> healthWorkerDTO = healthWorkerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(healthWorkerDTO);
    }

    /**
     * {@code DELETE  /health-workers/:id} : delete the "id" healthWorker.
     *
     * @param id the id of the healthWorkerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHealthWorker(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HealthWorker : {}", id);
        healthWorkerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

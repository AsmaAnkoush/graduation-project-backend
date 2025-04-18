package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.repository.VaccinationRepository;
import com.bzu.smartvax.service.VaccinationService;
import com.bzu.smartvax.service.dto.VaccinationDTO;
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
 * REST controller for managing {@link com.bzu.smartvax.domain.Vaccination}.
 */
@RestController
@RequestMapping("/api/vaccinations")
public class VaccinationResource {

    private static final Logger LOG = LoggerFactory.getLogger(VaccinationResource.class);

    private static final String ENTITY_NAME = "vaccination";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaccinationService vaccinationService;

    private final VaccinationRepository vaccinationRepository;

    public VaccinationResource(VaccinationService vaccinationService, VaccinationRepository vaccinationRepository) {
        this.vaccinationService = vaccinationService;
        this.vaccinationRepository = vaccinationRepository;
    }

    /**
     * {@code POST  /vaccinations} : Create a new vaccination.
     *
     * @param vaccinationDTO the vaccinationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaccinationDTO, or with status {@code 400 (Bad Request)} if the vaccination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VaccinationDTO> createVaccination(@Valid @RequestBody VaccinationDTO vaccinationDTO) throws URISyntaxException {
        LOG.debug("REST request to save Vaccination : {}", vaccinationDTO);
        if (vaccinationDTO.getId() != null) {
            throw new BadRequestAlertException("A new vaccination cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vaccinationDTO = vaccinationService.save(vaccinationDTO);
        return ResponseEntity.created(new URI("/api/vaccinations/" + vaccinationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vaccinationDTO.getId().toString()))
            .body(vaccinationDTO);
    }

    /**
     * {@code PUT  /vaccinations/:id} : Updates an existing vaccination.
     *
     * @param id the id of the vaccinationDTO to save.
     * @param vaccinationDTO the vaccinationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationDTO,
     * or with status {@code 400 (Bad Request)} if the vaccinationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VaccinationDTO> updateVaccination(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VaccinationDTO vaccinationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Vaccination : {}, {}", id, vaccinationDTO);
        if (vaccinationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vaccinationDTO = vaccinationService.update(vaccinationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaccinationDTO.getId().toString()))
            .body(vaccinationDTO);
    }

    /**
     * {@code PATCH  /vaccinations/:id} : Partial updates given fields of an existing vaccination, field will ignore if it is null
     *
     * @param id the id of the vaccinationDTO to save.
     * @param vaccinationDTO the vaccinationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationDTO,
     * or with status {@code 400 (Bad Request)} if the vaccinationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vaccinationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VaccinationDTO> partialUpdateVaccination(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VaccinationDTO vaccinationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Vaccination partially : {}, {}", id, vaccinationDTO);
        if (vaccinationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VaccinationDTO> result = vaccinationService.partialUpdate(vaccinationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaccinationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vaccinations} : get all the vaccinations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaccinations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VaccinationDTO>> getAllVaccinations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Vaccinations");
        Page<VaccinationDTO> page = vaccinationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vaccinations/:id} : get the "id" vaccination.
     *
     * @param id the id of the vaccinationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaccinationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VaccinationDTO> getVaccination(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Vaccination : {}", id);
        Optional<VaccinationDTO> vaccinationDTO = vaccinationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vaccinationDTO);
    }

    /**
     * {@code DELETE  /vaccinations/:id} : delete the "id" vaccination.
     *
     * @param id the id of the vaccinationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaccination(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Vaccination : {}", id);
        vaccinationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

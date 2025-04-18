//package com.bzu.smartvax.web.rest;
//
//import com.bzu.smartvax.repository.AIAnalyzerRepository;
//import com.bzu.smartvax.service.AIAnalyzerService;
//import com.bzu.smartvax.service.dto.AIAnalyzerDTO;
//import com.bzu.smartvax.web.rest.errors.BadRequestAlertException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//import tech.jhipster.web.util.HeaderUtil;
//import tech.jhipster.web.util.PaginationUtil;
//import tech.jhipster.web.util.ResponseUtil;
//
///**
// * REST controller for managing {@link com.bzu.smartvax.domain.AIAnalyzer}.
// */
//@RestController
//@RequestMapping("/api/ai-analyzers")
//public class AIAnalyzerResource {
//
//    private static final Logger LOG = LoggerFactory.getLogger(AIAnalyzerResource.class);
//
//    private static final String ENTITY_NAME = "aIAnalyzer";
//
//    @Value("${jhipster.clientApp.name}")
//    private String applicationName;
//
//    private final AIAnalyzerService aIAnalyzerService;
//
//    private final AIAnalyzerRepository aIAnalyzerRepository;
//
//    public AIAnalyzerResource(AIAnalyzerService aIAnalyzerService, AIAnalyzerRepository aIAnalyzerRepository) {
//        this.aIAnalyzerService = aIAnalyzerService;
//        this.aIAnalyzerRepository = aIAnalyzerRepository;
//    }
//
//    /**
//     * {@code POST  /ai-analyzers} : Create a new aIAnalyzer.
//     *
//     * @param aIAnalyzerDTO the aIAnalyzerDTO to create.
//     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aIAnalyzerDTO, or with status {@code 400 (Bad Request)} if the aIAnalyzer has already an ID.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PostMapping("")
//    public ResponseEntity<AIAnalyzerDTO> createAIAnalyzer(@RequestBody AIAnalyzerDTO aIAnalyzerDTO) throws URISyntaxException {
//        LOG.debug("REST request to save AIAnalyzer : {}", aIAnalyzerDTO);
//        if (aIAnalyzerDTO.getId() != null) {
//            throw new BadRequestAlertException("A new aIAnalyzer cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        aIAnalyzerDTO = aIAnalyzerService.save(aIAnalyzerDTO);
//        return ResponseEntity.created(new URI("/api/ai-analyzers/" + aIAnalyzerDTO.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aIAnalyzerDTO.getId().toString()))
//            .body(aIAnalyzerDTO);
//    }
//
//    /**
//     * {@code PUT  /ai-analyzers/:id} : Updates an existing aIAnalyzer.
//     *
//     * @param id the id of the aIAnalyzerDTO to save.
//     * @param aIAnalyzerDTO the aIAnalyzerDTO to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aIAnalyzerDTO,
//     * or with status {@code 400 (Bad Request)} if the aIAnalyzerDTO is not valid,
//     * or with status {@code 500 (Internal Server Error)} if the aIAnalyzerDTO couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<AIAnalyzerDTO> updateAIAnalyzer(
//        @PathVariable(value = "id", required = false) final Long id,
//        @RequestBody AIAnalyzerDTO aIAnalyzerDTO
//    ) throws URISyntaxException {
//        LOG.debug("REST request to update AIAnalyzer : {}, {}", id, aIAnalyzerDTO);
//        if (aIAnalyzerDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, aIAnalyzerDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!aIAnalyzerRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        aIAnalyzerDTO = aIAnalyzerService.update(aIAnalyzerDTO);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aIAnalyzerDTO.getId().toString()))
//            .body(aIAnalyzerDTO);
//    }
//
//    /**
//     * {@code PATCH  /ai-analyzers/:id} : Partial updates given fields of an existing aIAnalyzer, field will ignore if it is null
//     *
//     * @param id the id of the aIAnalyzerDTO to save.
//     * @param aIAnalyzerDTO the aIAnalyzerDTO to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aIAnalyzerDTO,
//     * or with status {@code 400 (Bad Request)} if the aIAnalyzerDTO is not valid,
//     * or with status {@code 404 (Not Found)} if the aIAnalyzerDTO is not found,
//     * or with status {@code 500 (Internal Server Error)} if the aIAnalyzerDTO couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
//    public ResponseEntity<AIAnalyzerDTO> partialUpdateAIAnalyzer(
//        @PathVariable(value = "id", required = false) final Long id,
//        @RequestBody AIAnalyzerDTO aIAnalyzerDTO
//    ) throws URISyntaxException {
//        LOG.debug("REST request to partial update AIAnalyzer partially : {}, {}", id, aIAnalyzerDTO);
//        if (aIAnalyzerDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, aIAnalyzerDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!aIAnalyzerRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<AIAnalyzerDTO> result = aIAnalyzerService.partialUpdate(aIAnalyzerDTO);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aIAnalyzerDTO.getId().toString())
//        );
//    }
//
//    /**
//     * {@code GET  /ai-analyzers} : get all the aIAnalyzers.
//     *
//     * @param pageable the pagination information.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aIAnalyzers in body.
//     */
//    @GetMapping("")
//    public ResponseEntity<List<AIAnalyzerDTO>> getAllAIAnalyzers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
//        LOG.debug("REST request to get a page of AIAnalyzers");
//        Page<AIAnalyzerDTO> page = aIAnalyzerService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//        return ResponseEntity.ok().headers(headers).body(page.getContent());
//    }
//
//    /**
//     * {@code GET  /ai-analyzers/:id} : get the "id" aIAnalyzer.
//     *
//     * @param id the id of the aIAnalyzerDTO to retrieve.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aIAnalyzerDTO, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<AIAnalyzerDTO> getAIAnalyzer(@PathVariable("id") Long id) {
//        LOG.debug("REST request to get AIAnalyzer : {}", id);
//        Optional<AIAnalyzerDTO> aIAnalyzerDTO = aIAnalyzerService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(aIAnalyzerDTO);
//    }
//
//    /**
//     * {@code DELETE  /ai-analyzers/:id} : delete the "id" aIAnalyzer.
//     *
//     * @param id the id of the aIAnalyzerDTO to delete.
//     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAIAnalyzer(@PathVariable("id") Long id) {
//        LOG.debug("REST request to delete AIAnalyzer : {}", id);
//        aIAnalyzerService.delete(id);
//        return ResponseEntity.noContent()
//            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//            .build();
//    }
//}

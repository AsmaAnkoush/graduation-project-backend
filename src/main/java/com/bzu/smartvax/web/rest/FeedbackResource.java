package com.bzu.smartvax.web.rest;

import com.bzu.smartvax.repository.FeedbackRepository;
import com.bzu.smartvax.service.FeedbackService;
import com.bzu.smartvax.service.dto.FeedbackAnalysisResponseDTO;
import com.bzu.smartvax.service.dto.FeedbackDTO;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // مهم: إضافة هذا الاستيراد
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bzu.smartvax.domain.Feedback}.
 */
@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackResource {

    private static final Logger LOG = LoggerFactory.getLogger(FeedbackResource.class);

    private static final String ENTITY_NAME = "feedback";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackService feedbackService;

    private final FeedbackRepository feedbackRepository;

    public FeedbackResource(FeedbackService feedbackService, FeedbackRepository feedbackRepository) {
        this.feedbackService = feedbackService;
        this.feedbackRepository = feedbackRepository;
    }

    @PostMapping("")
    public ResponseEntity<FeedbackAnalysisResponseDTO> createFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO) throws Exception {
        LOG.debug("REST request to submit symptoms for Feedback analysis : {}", feedbackDTO);
        if (feedbackDTO.getId() != null) {
            throw new BadRequestAlertException("A new feedback submission should not have an ID", ENTITY_NAME, "idexists");
        }
        FeedbackAnalysisResponseDTO analysisResult = feedbackService.submitFeedback(feedbackDTO);
        return ResponseEntity.ok(analysisResult);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackDTO> updateFeedback(
        @PathVariable(value = "id") final Long id,
        @Valid @RequestBody FeedbackDTO feedbackDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Feedback : {}, {}", id, feedbackDTO);
        if (feedbackDTO.getId() == null) {
            throw new BadRequestAlertException("Feedback ID must not be null for update operations.", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackDTO.getId())) {
            throw new BadRequestAlertException("ID in path and body must match.", ENTITY_NAME, "idmismatch");
        }

        if (!feedbackRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback with ID " + id + " not found.");
        }

        FeedbackDTO result = feedbackService.save(feedbackDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeedbackDTO> partialUpdateFeedback(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeedbackDTO feedbackDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Feedback partially : {}, {}", id, feedbackDTO);
        if (feedbackDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeedbackDTO> result = feedbackService.partialUpdate(feedbackDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /feedbacks/by-vaccination/{vaccineId}} : Get all the feedbacks for a specific vaccination.
     *
     * @param vaccineId the ID of the vaccination.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbacks in body.
     */
    @GetMapping(value = "/by-vaccination/{vaccineId}", produces = MediaType.APPLICATION_JSON_VALUE) // تأكد من وجود هذا الـ endpoint
    public ResponseEntity<List<FeedbackDTO>> getAllFeedbacksByVaccinationId(
        @PathVariable Long vaccineId,
        @ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of Feedbacks for Vaccination ID: {}", vaccineId);
        Page<FeedbackDTO> page = feedbackService.findAllByVaccinationId(vaccineId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> getFeedback(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Feedback : {}", id);
        Optional<FeedbackDTO> feedbackDTO = feedbackService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedbackDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Feedback : {}", id);
        feedbackService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

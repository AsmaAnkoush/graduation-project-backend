package com.bzu.smartvax.web.rest;

import static com.bzu.smartvax.domain.AIAnalyzerAsserts.*;
import static com.bzu.smartvax.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bzu.smartvax.IntegrationTest;
import com.bzu.smartvax.domain.AIAnalyzer;
import com.bzu.smartvax.repository.AIAnalyzerRepository;
import com.bzu.smartvax.service.dto.AIAnalyzerDTO;
import com.bzu.smartvax.service.mapper.AIAnalyzerMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AIAnalyzerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AIAnalyzerResourceIT {

    private static final String DEFAULT_ANALYSIS_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_ANALYSIS_RESULT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ai-analyzers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AIAnalyzerRepository aIAnalyzerRepository;

    @Autowired
    private AIAnalyzerMapper aIAnalyzerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAIAnalyzerMockMvc;

    private AIAnalyzer aIAnalyzer;

    private AIAnalyzer insertedAIAnalyzer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AIAnalyzer createEntity() {
        return new AIAnalyzer().analysisResult(DEFAULT_ANALYSIS_RESULT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AIAnalyzer createUpdatedEntity() {
        return new AIAnalyzer().analysisResult(UPDATED_ANALYSIS_RESULT);
    }

    @BeforeEach
    void initTest() {
        aIAnalyzer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAIAnalyzer != null) {
            aIAnalyzerRepository.delete(insertedAIAnalyzer);
            insertedAIAnalyzer = null;
        }
    }

    @Test
    @Transactional
    void createAIAnalyzer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AIAnalyzer
        AIAnalyzerDTO aIAnalyzerDTO = aIAnalyzerMapper.toDto(aIAnalyzer);
        var returnedAIAnalyzerDTO = om.readValue(
            restAIAnalyzerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIAnalyzerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AIAnalyzerDTO.class
        );

        // Validate the AIAnalyzer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAIAnalyzer = aIAnalyzerMapper.toEntity(returnedAIAnalyzerDTO);
        assertAIAnalyzerUpdatableFieldsEquals(returnedAIAnalyzer, getPersistedAIAnalyzer(returnedAIAnalyzer));

        insertedAIAnalyzer = returnedAIAnalyzer;
    }

    @Test
    @Transactional
    void createAIAnalyzerWithExistingId() throws Exception {
        // Create the AIAnalyzer with an existing ID
        aIAnalyzer.setId(1L);
        AIAnalyzerDTO aIAnalyzerDTO = aIAnalyzerMapper.toDto(aIAnalyzer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAIAnalyzerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIAnalyzerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AIAnalyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAIAnalyzers() throws Exception {
        // Initialize the database
        insertedAIAnalyzer = aIAnalyzerRepository.saveAndFlush(aIAnalyzer);

        // Get all the aIAnalyzerList
        restAIAnalyzerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aIAnalyzer.getId().intValue())))
            .andExpect(jsonPath("$.[*].analysisResult").value(hasItem(DEFAULT_ANALYSIS_RESULT)));
    }

    @Test
    @Transactional
    void getAIAnalyzer() throws Exception {
        // Initialize the database
        insertedAIAnalyzer = aIAnalyzerRepository.saveAndFlush(aIAnalyzer);

        // Get the aIAnalyzer
        restAIAnalyzerMockMvc
            .perform(get(ENTITY_API_URL_ID, aIAnalyzer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aIAnalyzer.getId().intValue()))
            .andExpect(jsonPath("$.analysisResult").value(DEFAULT_ANALYSIS_RESULT));
    }

    @Test
    @Transactional
    void getNonExistingAIAnalyzer() throws Exception {
        // Get the aIAnalyzer
        restAIAnalyzerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAIAnalyzer() throws Exception {
        // Initialize the database
        insertedAIAnalyzer = aIAnalyzerRepository.saveAndFlush(aIAnalyzer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIAnalyzer
        AIAnalyzer updatedAIAnalyzer = aIAnalyzerRepository.findById(aIAnalyzer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAIAnalyzer are not directly saved in db
        em.detach(updatedAIAnalyzer);
        updatedAIAnalyzer.analysisResult(UPDATED_ANALYSIS_RESULT);
        AIAnalyzerDTO aIAnalyzerDTO = aIAnalyzerMapper.toDto(updatedAIAnalyzer);

        restAIAnalyzerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aIAnalyzerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aIAnalyzerDTO))
            )
            .andExpect(status().isOk());

        // Validate the AIAnalyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAIAnalyzerToMatchAllProperties(updatedAIAnalyzer);
    }

    @Test
    @Transactional
    void putNonExistingAIAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIAnalyzer.setId(longCount.incrementAndGet());

        // Create the AIAnalyzer
        AIAnalyzerDTO aIAnalyzerDTO = aIAnalyzerMapper.toDto(aIAnalyzer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAIAnalyzerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aIAnalyzerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aIAnalyzerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIAnalyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAIAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIAnalyzer.setId(longCount.incrementAndGet());

        // Create the AIAnalyzer
        AIAnalyzerDTO aIAnalyzerDTO = aIAnalyzerMapper.toDto(aIAnalyzer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIAnalyzerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aIAnalyzerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIAnalyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAIAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIAnalyzer.setId(longCount.incrementAndGet());

        // Create the AIAnalyzer
        AIAnalyzerDTO aIAnalyzerDTO = aIAnalyzerMapper.toDto(aIAnalyzer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIAnalyzerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIAnalyzerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AIAnalyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAIAnalyzerWithPatch() throws Exception {
        // Initialize the database
        insertedAIAnalyzer = aIAnalyzerRepository.saveAndFlush(aIAnalyzer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIAnalyzer using partial update
        AIAnalyzer partialUpdatedAIAnalyzer = new AIAnalyzer();
        partialUpdatedAIAnalyzer.setId(aIAnalyzer.getId());

        restAIAnalyzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAIAnalyzer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAIAnalyzer))
            )
            .andExpect(status().isOk());

        // Validate the AIAnalyzer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAIAnalyzerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAIAnalyzer, aIAnalyzer),
            getPersistedAIAnalyzer(aIAnalyzer)
        );
    }

    @Test
    @Transactional
    void fullUpdateAIAnalyzerWithPatch() throws Exception {
        // Initialize the database
        insertedAIAnalyzer = aIAnalyzerRepository.saveAndFlush(aIAnalyzer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIAnalyzer using partial update
        AIAnalyzer partialUpdatedAIAnalyzer = new AIAnalyzer();
        partialUpdatedAIAnalyzer.setId(aIAnalyzer.getId());

        partialUpdatedAIAnalyzer.analysisResult(UPDATED_ANALYSIS_RESULT);

        restAIAnalyzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAIAnalyzer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAIAnalyzer))
            )
            .andExpect(status().isOk());

        // Validate the AIAnalyzer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAIAnalyzerUpdatableFieldsEquals(partialUpdatedAIAnalyzer, getPersistedAIAnalyzer(partialUpdatedAIAnalyzer));
    }

    @Test
    @Transactional
    void patchNonExistingAIAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIAnalyzer.setId(longCount.incrementAndGet());

        // Create the AIAnalyzer
        AIAnalyzerDTO aIAnalyzerDTO = aIAnalyzerMapper.toDto(aIAnalyzer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAIAnalyzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aIAnalyzerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aIAnalyzerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIAnalyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAIAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIAnalyzer.setId(longCount.incrementAndGet());

        // Create the AIAnalyzer
        AIAnalyzerDTO aIAnalyzerDTO = aIAnalyzerMapper.toDto(aIAnalyzer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIAnalyzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aIAnalyzerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIAnalyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAIAnalyzer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIAnalyzer.setId(longCount.incrementAndGet());

        // Create the AIAnalyzer
        AIAnalyzerDTO aIAnalyzerDTO = aIAnalyzerMapper.toDto(aIAnalyzer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIAnalyzerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aIAnalyzerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AIAnalyzer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAIAnalyzer() throws Exception {
        // Initialize the database
        insertedAIAnalyzer = aIAnalyzerRepository.saveAndFlush(aIAnalyzer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aIAnalyzer
        restAIAnalyzerMockMvc
            .perform(delete(ENTITY_API_URL_ID, aIAnalyzer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aIAnalyzerRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AIAnalyzer getPersistedAIAnalyzer(AIAnalyzer aIAnalyzer) {
        return aIAnalyzerRepository.findById(aIAnalyzer.getId()).orElseThrow();
    }

    protected void assertPersistedAIAnalyzerToMatchAllProperties(AIAnalyzer expectedAIAnalyzer) {
        assertAIAnalyzerAllPropertiesEquals(expectedAIAnalyzer, getPersistedAIAnalyzer(expectedAIAnalyzer));
    }

    protected void assertPersistedAIAnalyzerToMatchUpdatableProperties(AIAnalyzer expectedAIAnalyzer) {
        assertAIAnalyzerAllUpdatablePropertiesEquals(expectedAIAnalyzer, getPersistedAIAnalyzer(expectedAIAnalyzer));
    }
}

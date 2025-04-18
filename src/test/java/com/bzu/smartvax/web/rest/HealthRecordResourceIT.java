package com.bzu.smartvax.web.rest;

import static com.bzu.smartvax.domain.HealthRecordAsserts.*;
import static com.bzu.smartvax.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bzu.smartvax.IntegrationTest;
import com.bzu.smartvax.domain.HealthRecord;
import com.bzu.smartvax.repository.HealthRecordRepository;
import com.bzu.smartvax.service.dto.HealthRecordDTO;
import com.bzu.smartvax.service.mapper.HealthRecordMapper;
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
 * Integration tests for the {@link HealthRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HealthRecordResourceIT {

    private static final String DEFAULT_SENSITIVITY = "AAAAAAAAAA";
    private static final String UPDATED_SENSITIVITY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DIABETES = false;
    private static final Boolean UPDATED_DIABETES = true;

    private static final Boolean DEFAULT_HIGH_BLOOD_PRESSURE = false;
    private static final Boolean UPDATED_HIGH_BLOOD_PRESSURE = true;

    private static final String DEFAULT_GENETIC_DISEASES = "AAAAAAAAAA";
    private static final String UPDATED_GENETIC_DISEASES = "BBBBBBBBBB";

    private static final String DEFAULT_BLOOD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BLOOD_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/health-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private HealthRecordMapper healthRecordMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHealthRecordMockMvc;

    private HealthRecord healthRecord;

    private HealthRecord insertedHealthRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HealthRecord createEntity() {
        return new HealthRecord()
            .sensitivity(DEFAULT_SENSITIVITY)
            .diabetes(DEFAULT_DIABETES)
            .highBloodPressure(DEFAULT_HIGH_BLOOD_PRESSURE)
            .geneticDiseases(DEFAULT_GENETIC_DISEASES)
            .bloodType(DEFAULT_BLOOD_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HealthRecord createUpdatedEntity() {
        return new HealthRecord()
            .sensitivity(UPDATED_SENSITIVITY)
            .diabetes(UPDATED_DIABETES)
            .highBloodPressure(UPDATED_HIGH_BLOOD_PRESSURE)
            .geneticDiseases(UPDATED_GENETIC_DISEASES)
            .bloodType(UPDATED_BLOOD_TYPE);
    }

    @BeforeEach
    void initTest() {
        healthRecord = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHealthRecord != null) {
            healthRecordRepository.delete(insertedHealthRecord);
            insertedHealthRecord = null;
        }
    }

    @Test
    @Transactional
    void createHealthRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HealthRecord
        HealthRecordDTO healthRecordDTO = healthRecordMapper.toDto(healthRecord);
        var returnedHealthRecordDTO = om.readValue(
            restHealthRecordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthRecordDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HealthRecordDTO.class
        );

        // Validate the HealthRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHealthRecord = healthRecordMapper.toEntity(returnedHealthRecordDTO);
        assertHealthRecordUpdatableFieldsEquals(returnedHealthRecord, getPersistedHealthRecord(returnedHealthRecord));

        insertedHealthRecord = returnedHealthRecord;
    }

    @Test
    @Transactional
    void createHealthRecordWithExistingId() throws Exception {
        // Create the HealthRecord with an existing ID
        healthRecord.setId(1L);
        HealthRecordDTO healthRecordDTO = healthRecordMapper.toDto(healthRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHealthRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HealthRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHealthRecords() throws Exception {
        // Initialize the database
        insertedHealthRecord = healthRecordRepository.saveAndFlush(healthRecord);

        // Get all the healthRecordList
        restHealthRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(healthRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].sensitivity").value(hasItem(DEFAULT_SENSITIVITY)))
            .andExpect(jsonPath("$.[*].diabetes").value(hasItem(DEFAULT_DIABETES)))
            .andExpect(jsonPath("$.[*].highBloodPressure").value(hasItem(DEFAULT_HIGH_BLOOD_PRESSURE)))
            .andExpect(jsonPath("$.[*].geneticDiseases").value(hasItem(DEFAULT_GENETIC_DISEASES)))
            .andExpect(jsonPath("$.[*].bloodType").value(hasItem(DEFAULT_BLOOD_TYPE)));
    }

    @Test
    @Transactional
    void getHealthRecord() throws Exception {
        // Initialize the database
        insertedHealthRecord = healthRecordRepository.saveAndFlush(healthRecord);

        // Get the healthRecord
        restHealthRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, healthRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(healthRecord.getId().intValue()))
            .andExpect(jsonPath("$.sensitivity").value(DEFAULT_SENSITIVITY))
            .andExpect(jsonPath("$.diabetes").value(DEFAULT_DIABETES))
            .andExpect(jsonPath("$.highBloodPressure").value(DEFAULT_HIGH_BLOOD_PRESSURE))
            .andExpect(jsonPath("$.geneticDiseases").value(DEFAULT_GENETIC_DISEASES))
            .andExpect(jsonPath("$.bloodType").value(DEFAULT_BLOOD_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingHealthRecord() throws Exception {
        // Get the healthRecord
        restHealthRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHealthRecord() throws Exception {
        // Initialize the database
        insertedHealthRecord = healthRecordRepository.saveAndFlush(healthRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the healthRecord
        HealthRecord updatedHealthRecord = healthRecordRepository.findById(healthRecord.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHealthRecord are not directly saved in db
        em.detach(updatedHealthRecord);
        updatedHealthRecord
            .sensitivity(UPDATED_SENSITIVITY)
            .diabetes(UPDATED_DIABETES)
            .highBloodPressure(UPDATED_HIGH_BLOOD_PRESSURE)
            .geneticDiseases(UPDATED_GENETIC_DISEASES)
            .bloodType(UPDATED_BLOOD_TYPE);
        HealthRecordDTO healthRecordDTO = healthRecordMapper.toDto(updatedHealthRecord);

        restHealthRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, healthRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(healthRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the HealthRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHealthRecordToMatchAllProperties(updatedHealthRecord);
    }

    @Test
    @Transactional
    void putNonExistingHealthRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthRecord.setId(longCount.incrementAndGet());

        // Create the HealthRecord
        HealthRecordDTO healthRecordDTO = healthRecordMapper.toDto(healthRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHealthRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, healthRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(healthRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHealthRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthRecord.setId(longCount.incrementAndGet());

        // Create the HealthRecord
        HealthRecordDTO healthRecordDTO = healthRecordMapper.toDto(healthRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(healthRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHealthRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthRecord.setId(longCount.incrementAndGet());

        // Create the HealthRecord
        HealthRecordDTO healthRecordDTO = healthRecordMapper.toDto(healthRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HealthRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHealthRecordWithPatch() throws Exception {
        // Initialize the database
        insertedHealthRecord = healthRecordRepository.saveAndFlush(healthRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the healthRecord using partial update
        HealthRecord partialUpdatedHealthRecord = new HealthRecord();
        partialUpdatedHealthRecord.setId(healthRecord.getId());

        partialUpdatedHealthRecord.sensitivity(UPDATED_SENSITIVITY);

        restHealthRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHealthRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHealthRecord))
            )
            .andExpect(status().isOk());

        // Validate the HealthRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHealthRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHealthRecord, healthRecord),
            getPersistedHealthRecord(healthRecord)
        );
    }

    @Test
    @Transactional
    void fullUpdateHealthRecordWithPatch() throws Exception {
        // Initialize the database
        insertedHealthRecord = healthRecordRepository.saveAndFlush(healthRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the healthRecord using partial update
        HealthRecord partialUpdatedHealthRecord = new HealthRecord();
        partialUpdatedHealthRecord.setId(healthRecord.getId());

        partialUpdatedHealthRecord
            .sensitivity(UPDATED_SENSITIVITY)
            .diabetes(UPDATED_DIABETES)
            .highBloodPressure(UPDATED_HIGH_BLOOD_PRESSURE)
            .geneticDiseases(UPDATED_GENETIC_DISEASES)
            .bloodType(UPDATED_BLOOD_TYPE);

        restHealthRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHealthRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHealthRecord))
            )
            .andExpect(status().isOk());

        // Validate the HealthRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHealthRecordUpdatableFieldsEquals(partialUpdatedHealthRecord, getPersistedHealthRecord(partialUpdatedHealthRecord));
    }

    @Test
    @Transactional
    void patchNonExistingHealthRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthRecord.setId(longCount.incrementAndGet());

        // Create the HealthRecord
        HealthRecordDTO healthRecordDTO = healthRecordMapper.toDto(healthRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHealthRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, healthRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(healthRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHealthRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthRecord.setId(longCount.incrementAndGet());

        // Create the HealthRecord
        HealthRecordDTO healthRecordDTO = healthRecordMapper.toDto(healthRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(healthRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHealthRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthRecord.setId(longCount.incrementAndGet());

        // Create the HealthRecord
        HealthRecordDTO healthRecordDTO = healthRecordMapper.toDto(healthRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthRecordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(healthRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HealthRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHealthRecord() throws Exception {
        // Initialize the database
        insertedHealthRecord = healthRecordRepository.saveAndFlush(healthRecord);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the healthRecord
        restHealthRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, healthRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return healthRecordRepository.count();
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

    protected HealthRecord getPersistedHealthRecord(HealthRecord healthRecord) {
        return healthRecordRepository.findById(healthRecord.getId()).orElseThrow();
    }

    protected void assertPersistedHealthRecordToMatchAllProperties(HealthRecord expectedHealthRecord) {
        assertHealthRecordAllPropertiesEquals(expectedHealthRecord, getPersistedHealthRecord(expectedHealthRecord));
    }

    protected void assertPersistedHealthRecordToMatchUpdatableProperties(HealthRecord expectedHealthRecord) {
        assertHealthRecordAllUpdatablePropertiesEquals(expectedHealthRecord, getPersistedHealthRecord(expectedHealthRecord));
    }
}

package com.bzu.smartvax.web.rest;

import static com.bzu.smartvax.domain.ScheduleVaccinationAsserts.*;
import static com.bzu.smartvax.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bzu.smartvax.IntegrationTest;
import com.bzu.smartvax.domain.ScheduleVaccination;
import com.bzu.smartvax.repository.ScheduleVaccinationRepository;
import com.bzu.smartvax.service.dto.ScheduleVaccinationDTO;
import com.bzu.smartvax.service.mapper.ScheduleVaccinationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ScheduleVaccinationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduleVaccinationResourceIT {

    private static final LocalDate DEFAULT_SCHEDULED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SCHEDULED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/schedule-vaccinations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScheduleVaccinationRepository scheduleVaccinationRepository;

    @Autowired
    private ScheduleVaccinationMapper scheduleVaccinationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleVaccinationMockMvc;

    private ScheduleVaccination scheduleVaccination;

    private ScheduleVaccination insertedScheduleVaccination;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleVaccination createEntity() {
        return new ScheduleVaccination().scheduledDate(DEFAULT_SCHEDULED_DATE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleVaccination createUpdatedEntity() {
        return new ScheduleVaccination().scheduledDate(UPDATED_SCHEDULED_DATE).status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        scheduleVaccination = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedScheduleVaccination != null) {
            scheduleVaccinationRepository.delete(insertedScheduleVaccination);
            insertedScheduleVaccination = null;
        }
    }

    @Test
    @Transactional
    void createScheduleVaccination() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScheduleVaccination
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(scheduleVaccination);
        var returnedScheduleVaccinationDTO = om.readValue(
            restScheduleVaccinationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleVaccinationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScheduleVaccinationDTO.class
        );

        // Validate the ScheduleVaccination in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedScheduleVaccination = scheduleVaccinationMapper.toEntity(returnedScheduleVaccinationDTO);
        assertScheduleVaccinationUpdatableFieldsEquals(
            returnedScheduleVaccination,
            getPersistedScheduleVaccination(returnedScheduleVaccination)
        );

        insertedScheduleVaccination = returnedScheduleVaccination;
    }

    @Test
    @Transactional
    void createScheduleVaccinationWithExistingId() throws Exception {
        // Create the ScheduleVaccination with an existing ID
        scheduleVaccination.setId(1L);
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(scheduleVaccination);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleVaccinationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleVaccinationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduleVaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScheduledDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduleVaccination.setScheduledDate(null);

        // Create the ScheduleVaccination, which fails.
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(scheduleVaccination);

        restScheduleVaccinationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleVaccinationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduleVaccination.setStatus(null);

        // Create the ScheduleVaccination, which fails.
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(scheduleVaccination);

        restScheduleVaccinationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleVaccinationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScheduleVaccinations() throws Exception {
        // Initialize the database
        insertedScheduleVaccination = scheduleVaccinationRepository.saveAndFlush(scheduleVaccination);

        // Get all the scheduleVaccinationList
        restScheduleVaccinationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleVaccination.getId().intValue())))
            .andExpect(jsonPath("$.[*].scheduledDate").value(hasItem(DEFAULT_SCHEDULED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getScheduleVaccination() throws Exception {
        // Initialize the database
        insertedScheduleVaccination = scheduleVaccinationRepository.saveAndFlush(scheduleVaccination);

        // Get the scheduleVaccination
        restScheduleVaccinationMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduleVaccination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduleVaccination.getId().intValue()))
            .andExpect(jsonPath("$.scheduledDate").value(DEFAULT_SCHEDULED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingScheduleVaccination() throws Exception {
        // Get the scheduleVaccination
        restScheduleVaccinationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScheduleVaccination() throws Exception {
        // Initialize the database
        insertedScheduleVaccination = scheduleVaccinationRepository.saveAndFlush(scheduleVaccination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduleVaccination
        ScheduleVaccination updatedScheduleVaccination = scheduleVaccinationRepository.findById(scheduleVaccination.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScheduleVaccination are not directly saved in db
        em.detach(updatedScheduleVaccination);
        updatedScheduleVaccination.scheduledDate(UPDATED_SCHEDULED_DATE).status(UPDATED_STATUS);
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(updatedScheduleVaccination);

        restScheduleVaccinationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleVaccinationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduleVaccinationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleVaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScheduleVaccinationToMatchAllProperties(updatedScheduleVaccination);
    }

    @Test
    @Transactional
    void putNonExistingScheduleVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleVaccination.setId(longCount.incrementAndGet());

        // Create the ScheduleVaccination
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(scheduleVaccination);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleVaccinationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleVaccinationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduleVaccinationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleVaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduleVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleVaccination.setId(longCount.incrementAndGet());

        // Create the ScheduleVaccination
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(scheduleVaccination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleVaccinationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduleVaccinationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleVaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduleVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleVaccination.setId(longCount.incrementAndGet());

        // Create the ScheduleVaccination
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(scheduleVaccination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleVaccinationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleVaccinationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleVaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleVaccinationWithPatch() throws Exception {
        // Initialize the database
        insertedScheduleVaccination = scheduleVaccinationRepository.saveAndFlush(scheduleVaccination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduleVaccination using partial update
        ScheduleVaccination partialUpdatedScheduleVaccination = new ScheduleVaccination();
        partialUpdatedScheduleVaccination.setId(scheduleVaccination.getId());

        partialUpdatedScheduleVaccination.status(UPDATED_STATUS);

        restScheduleVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleVaccination.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScheduleVaccination))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleVaccination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduleVaccinationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedScheduleVaccination, scheduleVaccination),
            getPersistedScheduleVaccination(scheduleVaccination)
        );
    }

    @Test
    @Transactional
    void fullUpdateScheduleVaccinationWithPatch() throws Exception {
        // Initialize the database
        insertedScheduleVaccination = scheduleVaccinationRepository.saveAndFlush(scheduleVaccination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduleVaccination using partial update
        ScheduleVaccination partialUpdatedScheduleVaccination = new ScheduleVaccination();
        partialUpdatedScheduleVaccination.setId(scheduleVaccination.getId());

        partialUpdatedScheduleVaccination.scheduledDate(UPDATED_SCHEDULED_DATE).status(UPDATED_STATUS);

        restScheduleVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleVaccination.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScheduleVaccination))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleVaccination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduleVaccinationUpdatableFieldsEquals(
            partialUpdatedScheduleVaccination,
            getPersistedScheduleVaccination(partialUpdatedScheduleVaccination)
        );
    }

    @Test
    @Transactional
    void patchNonExistingScheduleVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleVaccination.setId(longCount.incrementAndGet());

        // Create the ScheduleVaccination
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(scheduleVaccination);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduleVaccinationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduleVaccinationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleVaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduleVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleVaccination.setId(longCount.incrementAndGet());

        // Create the ScheduleVaccination
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(scheduleVaccination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduleVaccinationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleVaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduleVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleVaccination.setId(longCount.incrementAndGet());

        // Create the ScheduleVaccination
        ScheduleVaccinationDTO scheduleVaccinationDTO = scheduleVaccinationMapper.toDto(scheduleVaccination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scheduleVaccinationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleVaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduleVaccination() throws Exception {
        // Initialize the database
        insertedScheduleVaccination = scheduleVaccinationRepository.saveAndFlush(scheduleVaccination);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scheduleVaccination
        restScheduleVaccinationMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduleVaccination.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scheduleVaccinationRepository.count();
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

    protected ScheduleVaccination getPersistedScheduleVaccination(ScheduleVaccination scheduleVaccination) {
        return scheduleVaccinationRepository.findById(scheduleVaccination.getId()).orElseThrow();
    }

    protected void assertPersistedScheduleVaccinationToMatchAllProperties(ScheduleVaccination expectedScheduleVaccination) {
        assertScheduleVaccinationAllPropertiesEquals(
            expectedScheduleVaccination,
            getPersistedScheduleVaccination(expectedScheduleVaccination)
        );
    }

    protected void assertPersistedScheduleVaccinationToMatchUpdatableProperties(ScheduleVaccination expectedScheduleVaccination) {
        assertScheduleVaccinationAllUpdatablePropertiesEquals(
            expectedScheduleVaccination,
            getPersistedScheduleVaccination(expectedScheduleVaccination)
        );
    }
}

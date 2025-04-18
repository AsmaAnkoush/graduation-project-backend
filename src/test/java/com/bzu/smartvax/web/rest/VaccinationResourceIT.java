package com.bzu.smartvax.web.rest;

import static com.bzu.smartvax.domain.VaccinationAsserts.*;
import static com.bzu.smartvax.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bzu.smartvax.IntegrationTest;
import com.bzu.smartvax.domain.Vaccination;
import com.bzu.smartvax.repository.VaccinationRepository;
import com.bzu.smartvax.service.dto.VaccinationDTO;
import com.bzu.smartvax.service.mapper.VaccinationMapper;
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
 * Integration tests for the {@link VaccinationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccinationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_GIVEN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_GIVEN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SIDE_EFFECTS = "AAAAAAAAAA";
    private static final String UPDATED_SIDE_EFFECTS = "BBBBBBBBBB";

    private static final Integer DEFAULT_TARGET_AGE = 1;
    private static final Integer UPDATED_TARGET_AGE = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_TREATMENT = "AAAAAAAAAA";
    private static final String UPDATED_TREATMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vaccinations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VaccinationRepository vaccinationRepository;

    @Autowired
    private VaccinationMapper vaccinationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccinationMockMvc;

    private Vaccination vaccination;

    private Vaccination insertedVaccination;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaccination createEntity() {
        return new Vaccination()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .dateGiven(DEFAULT_DATE_GIVEN)
            .sideEffects(DEFAULT_SIDE_EFFECTS)
            .targetAge(DEFAULT_TARGET_AGE)
            .status(DEFAULT_STATUS)
            .treatment(DEFAULT_TREATMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaccination createUpdatedEntity() {
        return new Vaccination()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .dateGiven(UPDATED_DATE_GIVEN)
            .sideEffects(UPDATED_SIDE_EFFECTS)
            .targetAge(UPDATED_TARGET_AGE)
            .status(UPDATED_STATUS)
            .treatment(UPDATED_TREATMENT);
    }

    @BeforeEach
    void initTest() {
        vaccination = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVaccination != null) {
            vaccinationRepository.delete(insertedVaccination);
            insertedVaccination = null;
        }
    }

    @Test
    @Transactional
    void createVaccination() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vaccination
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(vaccination);
        var returnedVaccinationDTO = om.readValue(
            restVaccinationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VaccinationDTO.class
        );

        // Validate the Vaccination in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVaccination = vaccinationMapper.toEntity(returnedVaccinationDTO);
        assertVaccinationUpdatableFieldsEquals(returnedVaccination, getPersistedVaccination(returnedVaccination));

        insertedVaccination = returnedVaccination;
    }

    @Test
    @Transactional
    void createVaccinationWithExistingId() throws Exception {
        // Create the Vaccination with an existing ID
        vaccination.setId(1L);
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(vaccination);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccinationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vaccination.setName(null);

        // Create the Vaccination, which fails.
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(vaccination);

        restVaccinationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vaccination.setStatus(null);

        // Create the Vaccination, which fails.
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(vaccination);

        restVaccinationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVaccinations() throws Exception {
        // Initialize the database
        insertedVaccination = vaccinationRepository.saveAndFlush(vaccination);

        // Get all the vaccinationList
        restVaccinationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccination.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].dateGiven").value(hasItem(DEFAULT_DATE_GIVEN.toString())))
            .andExpect(jsonPath("$.[*].sideEffects").value(hasItem(DEFAULT_SIDE_EFFECTS)))
            .andExpect(jsonPath("$.[*].targetAge").value(hasItem(DEFAULT_TARGET_AGE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].treatment").value(hasItem(DEFAULT_TREATMENT)));
    }

    @Test
    @Transactional
    void getVaccination() throws Exception {
        // Initialize the database
        insertedVaccination = vaccinationRepository.saveAndFlush(vaccination);

        // Get the vaccination
        restVaccinationMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccination.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.dateGiven").value(DEFAULT_DATE_GIVEN.toString()))
            .andExpect(jsonPath("$.sideEffects").value(DEFAULT_SIDE_EFFECTS))
            .andExpect(jsonPath("$.targetAge").value(DEFAULT_TARGET_AGE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.treatment").value(DEFAULT_TREATMENT));
    }

    @Test
    @Transactional
    void getNonExistingVaccination() throws Exception {
        // Get the vaccination
        restVaccinationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVaccination() throws Exception {
        // Initialize the database
        insertedVaccination = vaccinationRepository.saveAndFlush(vaccination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccination
        Vaccination updatedVaccination = vaccinationRepository.findById(vaccination.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVaccination are not directly saved in db
        em.detach(updatedVaccination);
        updatedVaccination
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .dateGiven(UPDATED_DATE_GIVEN)
            .sideEffects(UPDATED_SIDE_EFFECTS)
            .targetAge(UPDATED_TARGET_AGE)
            .status(UPDATED_STATUS)
            .treatment(UPDATED_TREATMENT);
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(updatedVaccination);

        restVaccinationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccinationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vaccinationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVaccinationToMatchAllProperties(updatedVaccination);
    }

    @Test
    @Transactional
    void putNonExistingVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccination.setId(longCount.incrementAndGet());

        // Create the Vaccination
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(vaccination);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccinationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vaccinationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccination.setId(longCount.incrementAndGet());

        // Create the Vaccination
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(vaccination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vaccinationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccination.setId(longCount.incrementAndGet());

        // Create the Vaccination
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(vaccination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccinationWithPatch() throws Exception {
        // Initialize the database
        insertedVaccination = vaccinationRepository.saveAndFlush(vaccination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccination using partial update
        Vaccination partialUpdatedVaccination = new Vaccination();
        partialUpdatedVaccination.setId(vaccination.getId());

        partialUpdatedVaccination.type(UPDATED_TYPE).treatment(UPDATED_TREATMENT);

        restVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccination.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVaccination))
            )
            .andExpect(status().isOk());

        // Validate the Vaccination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVaccinationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVaccination, vaccination),
            getPersistedVaccination(vaccination)
        );
    }

    @Test
    @Transactional
    void fullUpdateVaccinationWithPatch() throws Exception {
        // Initialize the database
        insertedVaccination = vaccinationRepository.saveAndFlush(vaccination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccination using partial update
        Vaccination partialUpdatedVaccination = new Vaccination();
        partialUpdatedVaccination.setId(vaccination.getId());

        partialUpdatedVaccination
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .dateGiven(UPDATED_DATE_GIVEN)
            .sideEffects(UPDATED_SIDE_EFFECTS)
            .targetAge(UPDATED_TARGET_AGE)
            .status(UPDATED_STATUS)
            .treatment(UPDATED_TREATMENT);

        restVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccination.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVaccination))
            )
            .andExpect(status().isOk());

        // Validate the Vaccination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVaccinationUpdatableFieldsEquals(partialUpdatedVaccination, getPersistedVaccination(partialUpdatedVaccination));
    }

    @Test
    @Transactional
    void patchNonExistingVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccination.setId(longCount.incrementAndGet());

        // Create the Vaccination
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(vaccination);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccinationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vaccinationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccination.setId(longCount.incrementAndGet());

        // Create the Vaccination
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(vaccination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vaccinationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccination.setId(longCount.incrementAndGet());

        // Create the Vaccination
        VaccinationDTO vaccinationDTO = vaccinationMapper.toDto(vaccination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vaccinationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaccination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccination() throws Exception {
        // Initialize the database
        insertedVaccination = vaccinationRepository.saveAndFlush(vaccination);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vaccination
        restVaccinationMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccination.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vaccinationRepository.count();
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

    protected Vaccination getPersistedVaccination(Vaccination vaccination) {
        return vaccinationRepository.findById(vaccination.getId()).orElseThrow();
    }

    protected void assertPersistedVaccinationToMatchAllProperties(Vaccination expectedVaccination) {
        assertVaccinationAllPropertiesEquals(expectedVaccination, getPersistedVaccination(expectedVaccination));
    }

    protected void assertPersistedVaccinationToMatchUpdatableProperties(Vaccination expectedVaccination) {
        assertVaccinationAllUpdatablePropertiesEquals(expectedVaccination, getPersistedVaccination(expectedVaccination));
    }
}

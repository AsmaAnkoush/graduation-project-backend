package com.bzu.smartvax.web.rest;

import static com.bzu.smartvax.domain.HealthWorkerAsserts.*;
import static com.bzu.smartvax.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bzu.smartvax.IntegrationTest;
import com.bzu.smartvax.domain.HealthWorker;
import com.bzu.smartvax.repository.HealthWorkerRepository;
import com.bzu.smartvax.service.dto.HealthWorkerDTO;
import com.bzu.smartvax.service.mapper.HealthWorkerMapper;
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
 * Integration tests for the {@link HealthWorkerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HealthWorkerResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/health-workers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HealthWorkerRepository healthWorkerRepository;

    @Autowired
    private HealthWorkerMapper healthWorkerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHealthWorkerMockMvc;

    private HealthWorker healthWorker;

    private HealthWorker insertedHealthWorker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HealthWorker createEntity() {
        return new HealthWorker()
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .phone(DEFAULT_PHONE)
            .age(DEFAULT_AGE)
            .name(DEFAULT_NAME)
            .gender(DEFAULT_GENDER)
            .location(DEFAULT_LOCATION)
            .email(DEFAULT_EMAIL)
            .role(DEFAULT_ROLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HealthWorker createUpdatedEntity() {
        return new HealthWorker()
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .phone(UPDATED_PHONE)
            .age(UPDATED_AGE)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .location(UPDATED_LOCATION)
            .email(UPDATED_EMAIL)
            .role(UPDATED_ROLE);
    }

    @BeforeEach
    void initTest() {
        healthWorker = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHealthWorker != null) {
            healthWorkerRepository.delete(insertedHealthWorker);
            insertedHealthWorker = null;
        }
    }

    @Test
    @Transactional
    void createHealthWorker() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HealthWorker
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);
        var returnedHealthWorkerDTO = om.readValue(
            restHealthWorkerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthWorkerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HealthWorkerDTO.class
        );

        // Validate the HealthWorker in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHealthWorker = healthWorkerMapper.toEntity(returnedHealthWorkerDTO);
        assertHealthWorkerUpdatableFieldsEquals(returnedHealthWorker, getPersistedHealthWorker(returnedHealthWorker));

        insertedHealthWorker = returnedHealthWorker;
    }

    @Test
    @Transactional
    void createHealthWorkerWithExistingId() throws Exception {
        // Create the HealthWorker with an existing ID
        healthWorker.setId(1L);
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHealthWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthWorkerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HealthWorker in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        healthWorker.setUsername(null);

        // Create the HealthWorker, which fails.
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        restHealthWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthWorkerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        healthWorker.setPassword(null);

        // Create the HealthWorker, which fails.
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        restHealthWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthWorkerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        healthWorker.setPhone(null);

        // Create the HealthWorker, which fails.
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        restHealthWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthWorkerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        healthWorker.setName(null);

        // Create the HealthWorker, which fails.
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        restHealthWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthWorkerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        healthWorker.setEmail(null);

        // Create the HealthWorker, which fails.
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        restHealthWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthWorkerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        healthWorker.setRole(null);

        // Create the HealthWorker, which fails.
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        restHealthWorkerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthWorkerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHealthWorkers() throws Exception {
        // Initialize the database
        insertedHealthWorker = healthWorkerRepository.saveAndFlush(healthWorker);

        // Get all the healthWorkerList
        restHealthWorkerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(healthWorker.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));
    }

    @Test
    @Transactional
    void getHealthWorker() throws Exception {
        // Initialize the database
        insertedHealthWorker = healthWorkerRepository.saveAndFlush(healthWorker);

        // Get the healthWorker
        restHealthWorkerMockMvc
            .perform(get(ENTITY_API_URL_ID, healthWorker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(healthWorker.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }

    @Test
    @Transactional
    void getNonExistingHealthWorker() throws Exception {
        // Get the healthWorker
        restHealthWorkerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHealthWorker() throws Exception {
        // Initialize the database
        insertedHealthWorker = healthWorkerRepository.saveAndFlush(healthWorker);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the healthWorker
        HealthWorker updatedHealthWorker = healthWorkerRepository.findById(healthWorker.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHealthWorker are not directly saved in db
        em.detach(updatedHealthWorker);
        updatedHealthWorker
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .phone(UPDATED_PHONE)
            .age(UPDATED_AGE)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .location(UPDATED_LOCATION)
            .email(UPDATED_EMAIL)
            .role(UPDATED_ROLE);
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(updatedHealthWorker);

        restHealthWorkerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, healthWorkerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(healthWorkerDTO))
            )
            .andExpect(status().isOk());

        // Validate the HealthWorker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHealthWorkerToMatchAllProperties(updatedHealthWorker);
    }

    @Test
    @Transactional
    void putNonExistingHealthWorker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthWorker.setId(longCount.incrementAndGet());

        // Create the HealthWorker
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHealthWorkerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, healthWorkerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(healthWorkerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthWorker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHealthWorker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthWorker.setId(longCount.incrementAndGet());

        // Create the HealthWorker
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthWorkerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(healthWorkerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthWorker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHealthWorker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthWorker.setId(longCount.incrementAndGet());

        // Create the HealthWorker
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthWorkerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(healthWorkerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HealthWorker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHealthWorkerWithPatch() throws Exception {
        // Initialize the database
        insertedHealthWorker = healthWorkerRepository.saveAndFlush(healthWorker);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the healthWorker using partial update
        HealthWorker partialUpdatedHealthWorker = new HealthWorker();
        partialUpdatedHealthWorker.setId(healthWorker.getId());

        partialUpdatedHealthWorker.password(UPDATED_PASSWORD).phone(UPDATED_PHONE).gender(UPDATED_GENDER).role(UPDATED_ROLE);

        restHealthWorkerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHealthWorker.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHealthWorker))
            )
            .andExpect(status().isOk());

        // Validate the HealthWorker in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHealthWorkerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHealthWorker, healthWorker),
            getPersistedHealthWorker(healthWorker)
        );
    }

    @Test
    @Transactional
    void fullUpdateHealthWorkerWithPatch() throws Exception {
        // Initialize the database
        insertedHealthWorker = healthWorkerRepository.saveAndFlush(healthWorker);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the healthWorker using partial update
        HealthWorker partialUpdatedHealthWorker = new HealthWorker();
        partialUpdatedHealthWorker.setId(healthWorker.getId());

        partialUpdatedHealthWorker
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .phone(UPDATED_PHONE)
            .age(UPDATED_AGE)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .location(UPDATED_LOCATION)
            .email(UPDATED_EMAIL)
            .role(UPDATED_ROLE);

        restHealthWorkerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHealthWorker.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHealthWorker))
            )
            .andExpect(status().isOk());

        // Validate the HealthWorker in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHealthWorkerUpdatableFieldsEquals(partialUpdatedHealthWorker, getPersistedHealthWorker(partialUpdatedHealthWorker));
    }

    @Test
    @Transactional
    void patchNonExistingHealthWorker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthWorker.setId(longCount.incrementAndGet());

        // Create the HealthWorker
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHealthWorkerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, healthWorkerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(healthWorkerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthWorker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHealthWorker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthWorker.setId(longCount.incrementAndGet());

        // Create the HealthWorker
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthWorkerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(healthWorkerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HealthWorker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHealthWorker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        healthWorker.setId(longCount.incrementAndGet());

        // Create the HealthWorker
        HealthWorkerDTO healthWorkerDTO = healthWorkerMapper.toDto(healthWorker);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHealthWorkerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(healthWorkerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HealthWorker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHealthWorker() throws Exception {
        // Initialize the database
        insertedHealthWorker = healthWorkerRepository.saveAndFlush(healthWorker);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the healthWorker
        restHealthWorkerMockMvc
            .perform(delete(ENTITY_API_URL_ID, healthWorker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return healthWorkerRepository.count();
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

    protected HealthWorker getPersistedHealthWorker(HealthWorker healthWorker) {
        return healthWorkerRepository.findById(healthWorker.getId()).orElseThrow();
    }

    protected void assertPersistedHealthWorkerToMatchAllProperties(HealthWorker expectedHealthWorker) {
        assertHealthWorkerAllPropertiesEquals(expectedHealthWorker, getPersistedHealthWorker(expectedHealthWorker));
    }

    protected void assertPersistedHealthWorkerToMatchUpdatableProperties(HealthWorker expectedHealthWorker) {
        assertHealthWorkerAllUpdatablePropertiesEquals(expectedHealthWorker, getPersistedHealthWorker(expectedHealthWorker));
    }
}

package com.bzu.smartvax.web.rest;

import static com.bzu.smartvax.domain.ChildAsserts.*;
import static com.bzu.smartvax.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bzu.smartvax.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bzu.smartvax.IntegrationTest;
import com.bzu.smartvax.domain.Child;
import com.bzu.smartvax.repository.ChildRepository;
import com.bzu.smartvax.service.dto.ChildDTO;
import com.bzu.smartvax.service.mapper.ChildMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ChildResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChildResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal(1);
    private static final BigDecimal UPDATED_WEIGHT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_HEIGHT = new BigDecimal(1);
    private static final BigDecimal UPDATED_HEIGHT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/children";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChildMockMvc;

    private Child child;

    private Child insertedChild;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Child createEntity() {
        return new Child().name(DEFAULT_NAME).dob(DEFAULT_DOB).weight(DEFAULT_WEIGHT).height(DEFAULT_HEIGHT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Child createUpdatedEntity() {
        return new Child().name(UPDATED_NAME).dob(UPDATED_DOB).weight(UPDATED_WEIGHT).height(UPDATED_HEIGHT);
    }

    @BeforeEach
    void initTest() {
        child = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedChild != null) {
            childRepository.delete(insertedChild);
            insertedChild = null;
        }
    }

    @Test
    @Transactional
    void createChild() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);
        var returnedChildDTO = om.readValue(
            restChildMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(childDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChildDTO.class
        );

        // Validate the Child in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChild = childMapper.toEntity(returnedChildDTO);
        assertChildUpdatableFieldsEquals(returnedChild, getPersistedChild(returnedChild));

        insertedChild = returnedChild;
    }

    @Test
    @Transactional
    void createChildWithExistingId() throws Exception {
        // Create the Child with an existing ID
        child.setId(1L);
        ChildDTO childDTO = childMapper.toDto(child);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(childDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        child.setName(null);

        // Create the Child, which fails.
        ChildDTO childDTO = childMapper.toDto(child);

        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(childDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDobIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        child.setDob(null);

        // Create the Child, which fails.
        ChildDTO childDTO = childMapper.toDto(child);

        restChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(childDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChildren() throws Exception {
        // Initialize the database
        insertedChild = childRepository.saveAndFlush(child);

        // Get all the childList
        restChildMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(child.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(sameNumber(DEFAULT_WEIGHT))))
            .andExpect(jsonPath("$.[*].height").value(hasItem(sameNumber(DEFAULT_HEIGHT))));
    }

    @Test
    @Transactional
    void getChild() throws Exception {
        // Initialize the database
        insertedChild = childRepository.saveAndFlush(child);

        // Get the child
        restChildMockMvc
            .perform(get(ENTITY_API_URL_ID, child.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(child.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.weight").value(sameNumber(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.height").value(sameNumber(DEFAULT_HEIGHT)));
    }

    @Test
    @Transactional
    void getNonExistingChild() throws Exception {
        // Get the child
        restChildMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChild() throws Exception {
        // Initialize the database
        insertedChild = childRepository.saveAndFlush(child);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the child
        Child updatedChild = childRepository.findById(child.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChild are not directly saved in db
        em.detach(updatedChild);
        updatedChild.name(UPDATED_NAME).dob(UPDATED_DOB).weight(UPDATED_WEIGHT).height(UPDATED_HEIGHT);
        ChildDTO childDTO = childMapper.toDto(updatedChild);

        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, childDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(childDTO))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChildToMatchAllProperties(updatedChild);
    }

    @Test
    @Transactional
    void putNonExistingChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        child.setId(longCount.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, childDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        child.setId(longCount.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        child.setId(longCount.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(childDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Child in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChildWithPatch() throws Exception {
        // Initialize the database
        insertedChild = childRepository.saveAndFlush(child);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the child using partial update
        Child partialUpdatedChild = new Child();
        partialUpdatedChild.setId(child.getId());

        partialUpdatedChild.name(UPDATED_NAME).height(UPDATED_HEIGHT);

        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChild.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChild))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChildUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedChild, child), getPersistedChild(child));
    }

    @Test
    @Transactional
    void fullUpdateChildWithPatch() throws Exception {
        // Initialize the database
        insertedChild = childRepository.saveAndFlush(child);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the child using partial update
        Child partialUpdatedChild = new Child();
        partialUpdatedChild.setId(child.getId());

        partialUpdatedChild.name(UPDATED_NAME).dob(UPDATED_DOB).weight(UPDATED_WEIGHT).height(UPDATED_HEIGHT);

        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChild.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChild))
            )
            .andExpect(status().isOk());

        // Validate the Child in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChildUpdatableFieldsEquals(partialUpdatedChild, getPersistedChild(partialUpdatedChild));
    }

    @Test
    @Transactional
    void patchNonExistingChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        child.setId(longCount.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, childDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        child.setId(longCount.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(childDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Child in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChild() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        child.setId(longCount.incrementAndGet());

        // Create the Child
        ChildDTO childDTO = childMapper.toDto(child);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChildMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(childDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Child in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChild() throws Exception {
        // Initialize the database
        insertedChild = childRepository.saveAndFlush(child);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the child
        restChildMockMvc
            .perform(delete(ENTITY_API_URL_ID, child.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return childRepository.count();
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

    protected Child getPersistedChild(Child child) {
        return childRepository.findById(child.getId()).orElseThrow();
    }

    protected void assertPersistedChildToMatchAllProperties(Child expectedChild) {
        assertChildAllPropertiesEquals(expectedChild, getPersistedChild(expectedChild));
    }

    protected void assertPersistedChildToMatchUpdatableProperties(Child expectedChild) {
        assertChildAllUpdatablePropertiesEquals(expectedChild, getPersistedChild(expectedChild));
    }
}

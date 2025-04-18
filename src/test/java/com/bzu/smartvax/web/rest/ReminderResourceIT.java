package com.bzu.smartvax.web.rest;

import static com.bzu.smartvax.domain.ReminderAsserts.*;
import static com.bzu.smartvax.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bzu.smartvax.IntegrationTest;
import com.bzu.smartvax.domain.Reminder;
import com.bzu.smartvax.repository.ReminderRepository;
import com.bzu.smartvax.service.dto.ReminderDTO;
import com.bzu.smartvax.service.mapper.ReminderMapper;
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
 * Integration tests for the {@link ReminderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReminderResourceIT {

    private static final String DEFAULT_MESSAGE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reminders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderMapper reminderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReminderMockMvc;

    private Reminder reminder;

    private Reminder insertedReminder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reminder createEntity() {
        return new Reminder().messageText(DEFAULT_MESSAGE_TEXT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reminder createUpdatedEntity() {
        return new Reminder().messageText(UPDATED_MESSAGE_TEXT);
    }

    @BeforeEach
    void initTest() {
        reminder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReminder != null) {
            reminderRepository.delete(insertedReminder);
            insertedReminder = null;
        }
    }

    @Test
    @Transactional
    void createReminder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);
        var returnedReminderDTO = om.readValue(
            restReminderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reminderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReminderDTO.class
        );

        // Validate the Reminder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReminder = reminderMapper.toEntity(returnedReminderDTO);
        assertReminderUpdatableFieldsEquals(returnedReminder, getPersistedReminder(returnedReminder));

        insertedReminder = returnedReminder;
    }

    @Test
    @Transactional
    void createReminderWithExistingId() throws Exception {
        // Create the Reminder with an existing ID
        reminder.setId(1L);
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReminderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reminderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMessageTextIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reminder.setMessageText(null);

        // Create the Reminder, which fails.
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        restReminderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reminderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReminders() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList
        restReminderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reminder.getId().intValue())))
            .andExpect(jsonPath("$.[*].messageText").value(hasItem(DEFAULT_MESSAGE_TEXT)));
    }

    @Test
    @Transactional
    void getReminder() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get the reminder
        restReminderMockMvc
            .perform(get(ENTITY_API_URL_ID, reminder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reminder.getId().intValue()))
            .andExpect(jsonPath("$.messageText").value(DEFAULT_MESSAGE_TEXT));
    }

    @Test
    @Transactional
    void getNonExistingReminder() throws Exception {
        // Get the reminder
        restReminderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReminder() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reminder
        Reminder updatedReminder = reminderRepository.findById(reminder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReminder are not directly saved in db
        em.detach(updatedReminder);
        updatedReminder.messageText(UPDATED_MESSAGE_TEXT);
        ReminderDTO reminderDTO = reminderMapper.toDto(updatedReminder);

        restReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reminderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reminderDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReminderToMatchAllProperties(updatedReminder);
    }

    @Test
    @Transactional
    void putNonExistingReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reminderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reminderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReminderWithPatch() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reminder using partial update
        Reminder partialUpdatedReminder = new Reminder();
        partialUpdatedReminder.setId(reminder.getId());

        partialUpdatedReminder.messageText(UPDATED_MESSAGE_TEXT);

        restReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReminder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReminder))
            )
            .andExpect(status().isOk());

        // Validate the Reminder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReminderUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReminder, reminder), getPersistedReminder(reminder));
    }

    @Test
    @Transactional
    void fullUpdateReminderWithPatch() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reminder using partial update
        Reminder partialUpdatedReminder = new Reminder();
        partialUpdatedReminder.setId(reminder.getId());

        partialUpdatedReminder.messageText(UPDATED_MESSAGE_TEXT);

        restReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReminder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReminder))
            )
            .andExpect(status().isOk());

        // Validate the Reminder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReminderUpdatableFieldsEquals(partialUpdatedReminder, getPersistedReminder(partialUpdatedReminder));
    }

    @Test
    @Transactional
    void patchNonExistingReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reminderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reminderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReminder() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reminder
        restReminderMockMvc
            .perform(delete(ENTITY_API_URL_ID, reminder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reminderRepository.count();
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

    protected Reminder getPersistedReminder(Reminder reminder) {
        return reminderRepository.findById(reminder.getId()).orElseThrow();
    }

    protected void assertPersistedReminderToMatchAllProperties(Reminder expectedReminder) {
        assertReminderAllPropertiesEquals(expectedReminder, getPersistedReminder(expectedReminder));
    }

    protected void assertPersistedReminderToMatchUpdatableProperties(Reminder expectedReminder) {
        assertReminderAllUpdatablePropertiesEquals(expectedReminder, getPersistedReminder(expectedReminder));
    }
}

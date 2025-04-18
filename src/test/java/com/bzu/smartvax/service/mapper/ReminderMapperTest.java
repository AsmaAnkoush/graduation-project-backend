package com.bzu.smartvax.service.mapper;

import static com.bzu.smartvax.domain.ReminderAsserts.*;
import static com.bzu.smartvax.domain.ReminderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReminderMapperTest {

    private ReminderMapper reminderMapper;

    @BeforeEach
    void setUp() {
        reminderMapper = new ReminderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReminderSample1();
        var actual = reminderMapper.toEntity(reminderMapper.toDto(expected));
        assertReminderAllPropertiesEquals(expected, actual);
    }
}
